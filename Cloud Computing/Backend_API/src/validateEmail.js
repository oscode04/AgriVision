const axios = require("axios");

//Fungsi untuk memvalidasi format email harus (Gmail/Yahoo)
exports.validateEmail = (email) => {
  const gmailOrYahooRegex = /^[a-zA-Z0-9._%+-]+@(gmail\.com|yahoo\.com)$/i;
  return gmailOrYahooRegex.test(email);
};

//Fungsi untuk memvalidasi keberadaan email menggunakan Abstract API
exports.checkEmailExistence = async (email) => {
  try {
    const apiKey = process.env.ABSTRAKMAIL_API_KEY;
    const response = await axios.get(
      `https://emailvalidation.abstractapi.com/v1/?api_key=${apiKey}&email=${email}`
    );

    //Cek hasil validasi
    const isDeliverable = response.data.deliverability === "DELIVERABLE";
    return isDeliverable;
  } catch (error) {
    console.error("Error validating email:", error.message);
    return false; 
  }
};

exports.validateEmailMiddleware = async (req, res, next) => {
  const { email } = req.body;

  if (!email) {
    return res.status(400).json({ error: "Email harus diisi." });
  }

  //Cek format email
  if (!exports.validateEmail(email)) {
    return res
      .status(400)
      .json({ error: "Format Email salah. Email harus valid dari Gmail atau Yahoo." });
  }

  //Cek keberadaan email
  const emailExists = await exports.checkEmailExistence(email);
  if (!emailExists) {
    return res.status(400).json({ error: "Email does not exist or is inactive." });
  }

  next();
};
