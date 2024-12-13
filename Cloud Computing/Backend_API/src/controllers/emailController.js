const db = require('../db');

exports.verifyEmail = async (req, res) => {
  const { username, verificationCode } = req.body;

  if (!username || !verificationCode) {
    return res.status(400).json({ error: 'Semua field harus diisi' });
  }

  try {
    const userRef = db.collection('users').doc(username);
    const userDoc = await userRef.get();

    if (!userDoc.exists) {
      return res.status(404).json({ error: 'Pengguna tidak ditemukan' });
    }

    const userData = userDoc.data();
    if (userData.verificationCode !== verificationCode) {
      return res.status(400).json({ error: 'Kode verifikasi salah' });
    }

    await userRef.update({ isVerified: true, verificationCode: null });

    return res.status(200).json({ message: 'Email berhasil diverifikasi' });
  } catch (error) {
    console.error(error);
    return res.status(500).json({ error: 'Terjadi kesalahan server' });
  }
};
