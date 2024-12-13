const bcrypt = require('bcrypt');
const db = require('../db');
const { validateEmail, checkEmailExistence } = require('../validateEmail');
const { v4: uuidv4 } = require('uuid');
const { sendVerificationEmail } = require('../emailConfig');
console.log(validateEmail('test@gmail.com')); 
console.log(validateEmail('test@yahoo.com'));
console.log(validateEmail('test@hotmail.com'));


//Fungsi buat registrasi pengguna
exports.registerUser = async (req, res) => {
  const { email, username, password, nickname } = req.body;
  if (!email || !username || !password || !nickname) {
    return res.status(400).json({ error: 'Semua field harus diisi' });
  }

  if (!validateEmail(email)) {
    return res.status(400).json({ error: 'Format Email salah. Email harus valid dari Gmail atau Yahoo' });
  }

  //Validasi cek email pake Abstract API
  const emailExists = await checkEmailExistence(email);
  if (!emailExists) {
    return res.status(400).json({ error: 'Email tidak valid atau tidak aktif' });
  }

  try {
    const usersRef = db.collection('users');
    const emailExists = await usersRef.where('email', '==', email).get();
    const usernameExists = await usersRef.doc(username).get();

    if (!emailExists.empty || usernameExists.exists) {
      return res.status(400).json({ error: 'Email atau username sudah digunakan' });
    }

    const hashedPassword = await bcrypt.hash(password, 10);
    const verificationCode = uuidv4().slice(0, 6).toUpperCase();

    await usersRef.doc(username).set({
      email,
      username,
      password: hashedPassword,
      nickname,
      isVerified: false,
      verificationCode,
    });

    await sendVerificationEmail(email, nickname, verificationCode, 'registration');
    return res.status(201).json({ message: 'Registrasi berhasil, cek email untuk verifikasi' });
  } catch (error) {
    console.error(error);
    return res.status(500).json({ error: 'Terjadi kesalahan server' });
  }
};

//Fungsi login
exports.loginUser = async (req, res) => {
  const { emailOrUsername, password } = req.body;

  if (!emailOrUsername || !password) {
    return res.status(400).json({ error: 'Semua field harus diisi' });
  }

  try {
    const usersRef = db.collection('users');
    let userDoc;

    if (emailOrUsername.includes('@')) {
      const userSnapshot = await usersRef.where('email', '==', emailOrUsername).get();
      if (!userSnapshot.empty) {
        userDoc = userSnapshot.docs[0];
      }
    } else {
      userDoc = await usersRef.doc(emailOrUsername).get();
    }

    if (!userDoc || !userDoc.exists) {
      return res.status(401).json({ error: 'Email atau username tidak ditemukan' });
    }

    const userData = userDoc.data();
    const isPasswordValid = await bcrypt.compare(password, userData.password);

    if (!isPasswordValid) {
      return res.status(401).json({ error: 'Password salah' });
    }

    if (!userData.isVerified) {
      return res.status(401).json({ error: 'Email belum diverifikasi' });
    }
    return res.status(200).json({
      message: 'Login berhasil',
      email: userData.email,
      username: userData.username,
      nickname: userData.nickname,
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({ error: 'Terjadi kesalahan server' });
  }
};


//Fungsi buat hapus akun pake password
exports.deleteUser = async (req, res) => {
  const { username } = req.params;
  const { password } = req.body;

  if (!username || !password) {
    return res.status(400).json({ error: 'Username dan password harus disertakan' });
  }

  try {
    const userRef = db.collection('users').doc(username);
    const userDoc = await userRef.get();

    if (!userDoc.exists) {
      return res.status(404).json({ error: 'Akun dengan username tersebut tidak ditemukan' });
    }

    const userData = userDoc.data();

    //Verifikasi password
    const isPasswordValid = await bcrypt.compare(password, userData.password);
    if (!isPasswordValid) {
      return res.status(401).json({ error: 'Password saat ini salah' });
    }

    //Hapus akun dari database
    await userRef.delete();

    return res.status(200).json({ message: `Akun dengan username '${username}' berhasil dihapus` });
  } catch (error) {
    console.error('Error saat menghapus akun:', error);
    return res.status(500).json({ error: 'Terjadi kesalahan pada server' });
  }
};


//Fungsi buat update profil pengguna
exports.updateProfile = async (req, res) => {
  const { username } = req.params;
  const { nickname, newUsername, oldPassword, newPassword, confirmPassword, newEmail, verificationCode } = req.body;

  if (!username) {
    return res.status(400).json({ error: 'Username harus disertakan di parameter URL.' });
  }

  try {
    const userRef = db.collection('users').doc(username);
    const userDoc = await userRef.get();

    if (!userDoc.exists) {
      return res.status(404).json({ error: 'Akun dengan username tersebut tidak ditemukan.' });
    }

    const userData = userDoc.data();

    //Update nickname
    if (nickname) {
      await userRef.update({ nickname });
    }

    //Update username
    if (newUsername) {
      const usernameExists = await db.collection('users').doc(newUsername).get();
      if (usernameExists.exists) {
        return res.status(400).json({ error: 'Username sudah digunakan.' });
      }
      await userRef.delete(); 
      await db.collection('users').doc(newUsername).set({ ...userData, username: newUsername });
    }

    //Ganti password kalau tidak lupa
    if (newPassword) {
      if (oldPassword) {
        const isPasswordValid = await bcrypt.compare(oldPassword, userData.password);
        if (!isPasswordValid) {
          return res.status(401).json({ error: 'Password lama salah.' });
        }
      } else if (verificationCode !== userData.verificationCode) {
        return res.status(400).json({ error: 'Kode verifikasi salah.' });
      }

      if (newPassword !== confirmPassword) {
        return res.status(400).json({ error: 'Password baru dan konfirmasi password tidak cocok.' });
      }

      const hashedNewPassword = await bcrypt.hash(newPassword, 10);
      await userRef.update({ password: hashedNewPassword });
    }

    //Ganti email dengan validasi password dan kode verifikasi
    if (newEmail) {
      if (!oldPassword) {
        return res.status(400).json({ error: 'Password saat ini diperlukan untuk mengganti email.' });
      }

      //Verifikasi password lama
      const isPasswordValid = await bcrypt.compare(oldPassword, userData.password);
      if (!isPasswordValid) {
        return res.status(401).json({ error: 'Password saat ini salah.' });
      }

      //Verifikasi kode
      if (verificationCode !== userData['verifcode-for-changeemail']) {
        return res.status(400).json({ error: 'Kode verifikasi salah.' });
      }

      //Update email
      await userRef.update({
        email: newEmail,
        'verifcode-for-changeemail': null,
      });
    }

    return res.status(200).json({ message: 'Profil berhasil diperbarui.' });
  } catch (error) {
    console.error('Error saat memperbarui profil:', error);
    return res.status(500).json({ error: 'Terjadi kesalahan pada server.' });
  }
};


//Fungsi untuk reset password pengguna jika lupa password
exports.resetPassword = async (req, res) => {
  const { email, newPassword, confirmPassword, verificationCode } = req.body;

  if (!email || !newPassword || !confirmPassword || !verificationCode) {
    return res.status(400).json({ error: 'Semua field harus diisi.' });
  }

  if (newPassword !== confirmPassword) {
    return res.status(400).json({ error: 'Password baru dan konfirmasi password tidak cocok.' });
  }

  try {
    const usersRef = db.collection('users');
    const userSnapshot = await usersRef.where('email', '==', email).get();

    if (userSnapshot.empty) {
      return res.status(404).json({ error: 'Email tidak ditemukan.' });
    }

    const userDoc = userSnapshot.docs[0];
    const userRef = userDoc.ref;
    const userData = userDoc.data();

    if (verificationCode !== userData['verifcode-for-resetpassword']) {
      return res.status(400).json({ error: 'Kode verifikasi salah.' });
    }

    //Hash password baru dan perbarui di Firestore
    const hashedNewPassword = await bcrypt.hash(newPassword, 10);
    await userRef.update({
      password: hashedNewPassword,
      'verifcode-for-resetpassword': null,
    });

    return res.status(200).json({ message: 'Password berhasil direset.' });
  } catch (error) {
    console.error('Error saat reset password:', error);
    return res.status(500).json({ error: 'Terjadi kesalahan pada server.' });
  }
};



//fungsi buat minta kode verifikasi ganti email atau reset password
exports.requestVerificationCode = async (req, res) => {
  const { username, email, actionType, newEmail } = req.body;

  if (!username && !email) {
    return res.status(400).json({ error: 'Username atau email harus disertakan.' });
  }

  try {
    let userRef;
    
    if (username) {
      userRef = db.collection('users').doc(username);
    } else {
      const snapshot = await db.collection('users').where('email', '==', email).get();
      if (snapshot.empty) {
        return res.status(404).json({ error: 'Akun dengan email tersebut tidak ditemukan.' });
      }
      userRef = snapshot.docs[0].ref;
    }

    const userDoc = await userRef.get();
    if (!userDoc.exists) {
      return res.status(404).json({ error: 'Akun tidak ditemukan.' });
    }

    const userData = userDoc.data();
    const verificationCode = uuidv4().slice(0, 6).toUpperCase();

    switch (actionType) {
      case 'passwordReset':
        //Tambahkan kode untuk reset password
        await userRef.update({ 'verifcode-for-resetpassword': verificationCode });
        await sendVerificationEmail(userData.email, userData.nickname, verificationCode, 'passwordReset');
        break;

      case 'emailChange':
        if (!newEmail) {
          return res.status(400).json({ error: 'Email baru harus disertakan untuk perubahan email.' });
        }

        //Pastikan email baru belum digunakan
        const emailSnapshot = await db.collection('users').where('email', '==', newEmail).get();
        if (!emailSnapshot.empty) {
          return res.status(400).json({ error: 'Email baru sudah digunakan oleh pengguna lain.' });
        }

        //Simpan email baru dan kirim kode verifikasi ke email baru
        await userRef.update({
          'verifcode-for-changeemail': verificationCode,
          'pendingNewEmail': newEmail,
        });
        await sendVerificationEmail(newEmail, userData.nickname, verificationCode, 'emailChange');
        break;

      default:
        return res.status(400).json({ error: 'Tipe aksi tidak valid.' });
    }

    return res.status(200).json({ message: 'Kode verifikasi telah dikirim.' });
  } catch (error) {
    console.error('Error saat mengirim kode verifikasi:', error);
    return res.status(500).json({ error: 'Terjadi kesalahan pada server.' });
  }
};
