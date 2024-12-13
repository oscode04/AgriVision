const nodemailer = require('nodemailer');
require('dotenv').config();

const transporter = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: "agrivision.technology@gmail.com",
    pass: process.env.EMAIL_PASSWORD, 
  }
});

exports.sendVerificationEmail = async (email, nickname, code, emailType) => {
  try {
    let subject = '';
    let htmlContent = '';

    switch (emailType) {
      case 'registration':
        subject = 'Verifikasi Email Registrasi Agrivision';
        htmlContent = `
          <div style="font-family: Arial, sans-serif; color: #333; line-height: 1.6;">
    <h2 style="color: #468a48;">Halo ${nickname},</h2>
    <p>Terima kasih telah mendaftar di <strong>Agrivision</strong>. Kami sangat senang Anda bergabung!</p>
    <p>Berikut adalah kode verifikasi Anda:</p>
    <div style="text-align: center; margin: 20px 0;">
      <span style="display: inline-block; font-size: 24px; font-weight: bold; color: #468a48; padding: 10px 20px; border: 1px solid #468a48; border-radius: 5px;">
        ${code}
      </span>
    </div>
    <p>Gunakan kode ini untuk menyelesaikan proses verifikasi email Anda. Jika Anda tidak merasa mendaftar di Agrivision, silakan abaikan email ini.</p>
    <p>Salam hangat,</p>
    <p><strong>Team Agrivision</strong></p>
    <hr style="border: 0; height: 1px; background: #ddd; margin: 20px 0;">
    <p style="font-size: 12px; color: #999;">Email ini dikirim secara otomatis. Harap tidak membalas email ini.</p>
  </div>
        `;
        break;
      case 'passwordReset':
        subject = 'Reset Password Akun Agrivision';
        htmlContent = `
            <div style="font-family: Arial, sans-serif; color: #333; line-height: 1.6;">
    <h2 style="color: #468a48;">Halo ${nickname},</h2>
    <p>Kami menerima permintaan untuk mereset password akun Anda di <strong>Agrivision</strong>.</p>
    <p>Berikut adalah kode verifikasi Anda:</p>
    <div style="text-align: center; margin: 20px 0;">
      <span style="display: inline-block; font-size: 24px; font-weight: bold; color: #468a48; padding: 10px 20px; border: 1px solid #468a48; border-radius: 5px;">
        ${code}
      </span>
    </div>
    <p>Gunakan kode ini untuk menyelesaikan proses reset password Anda. Jika Anda tidak merasa meminta reset password, silakan abaikan email ini.</p>
    <p>Salam hangat,</p>
    <p><strong>Team Agrivision</strong></p>
    <hr style="border: 0; height: 1px; background: #ddd; margin: 20px 0;">
    <p style="font-size: 12px; color: #999;">Email ini dikirim secara otomatis. Harap tidak membalas email ini.</p>
</div>
        `;
        break;
      case 'emailChange':
        subject = 'Verifikasi Perubahan Email Agrivision';
        htmlContent = `
          <div style="font-family: Arial, sans-serif; color: #333; line-height: 1.6;">
    <h2 style="color: #468a48;">Halo ${nickname},</h2>
    <p>Kami menerima permintaan untuk mengganti email akun Anda di <strong>Agrivision</strong>.</p>
    <p>Berikut adalah kode verifikasi Anda:</p>
    <div style="text-align: center; margin: 20px 0;">
      <span style="display: inline-block; font-size: 24px; font-weight: bold; color: #468a48; padding: 10px 20px; border: 1px solid #468a48; border-radius: 5px;">
        ${code}
      </span>
    </div>
    <p>Gunakan kode ini untuk menyelesaikan proses penggantian email Anda. Jika Anda tidak merasa meminta penggantian email, silakan abaikan email ini.</p>
    <p>Salam hangat,</p>
    <p><strong>Team Agrivision</strong></p>
    <hr style="border: 0; height: 1px; background: #ddd; margin: 20px 0;">
    <p style="font-size: 12px; color: #999;">Email ini dikirim secara otomatis. Harap tidak membalas email ini.</p>
</div>

        `;
        break;
      default:
        throw new Error('Tipe email tidak dikenali.');
    }

    const mailOptions = {
      from: 'TEAM AGRIVISION (agrivision.technology@gmail.com)',
      to: email,
      subject: subject,
      html: htmlContent,
    };

    const info = await transporter.sendMail(mailOptions);
    console.log('Email terkirim: ', info.response);
  } catch (error) {
    console.error('Error sending email: ', error.message);
  }
};

transporter.verify((error, success) => {
  if (error) {
    console.log("Error connecting to the email service: ", error);
  } else {
    console.log("Email service is ready to take messages.");
  }
});
