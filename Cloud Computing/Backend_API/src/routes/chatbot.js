const express = require('express');
const router = express.Router();
const axios = require('axios');

const API_KEY = process.env.CHATBOT_API_KEY;
const API_ENDPOINT = `https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=${API_KEY}`;

async function sendMessageToGemini(userMessage) {
  const agriculturePrompt = `PERKENALKAN DIRI ANDA SEBAGAI CHATBOT AGRIVISION. Anda jawab sesuai dengan bahasa pertanyaan user, jika user tanya dengan bahasa indonesia jawablah dengan indonesia, dan begitu juga bahasa inggris. Jika user tanya dengan Bahasa inggris jawablah dengan Bahasa inggris, tolong sesuaikan dengan bahasa user. Jangan jawab pertanyaan mengandung kekerasan, kebencian, pornografi, rasisme, pembunuhan, hacker, dan sexual eksplisit. Jadi Anda adalah seorang ahli di bidang pertanian, perkebunan, berkebun, pupuk, dan topik-topik terkait. Mohon hanya menjawab pertanyaan yang berkaitan dengan bidang-bidang tersebut seperti: tips pertanian, berkebun, budidaya tanaman, dan saran pemupukan. TOLONG HANYA JAWAB PERTANYAAN TENTANG AGRIKULTUR, PERTANIAN, PUPUK PERTANIAN, PERKEBUNAN, TIPS PERTANIAN. INTINYA BERCOCOK TANAM ATAU BERKEBUN ATAU PERTANIAN. SELAIN DARI ITU, TIDAH USAH JAWAB, TOLAK SAJA. Jika pertanyaan User sesuai dengan rules tersebut maka tolong jawab dengan awalan "Halo Agrimin disini, ini jawaban kamu" lalu barus baru. Namun, jika pertanyaan user tidak sesuai dengan rules tersebut, tolong jawab dengan kalimat "Maaf, pertanyaan tersebut di luar bidang keahlian Agrimin. Agrimin hanya dapat menjawab pertanyaan seputar pertanian, perkebunan dan topik-topik terkait". Berikan jawaban sebagai teks biasa, tanpa tanda bintang, tanpa format markdown atau HTML, dan tanpa heading. OKE INI PERTANYAANYA.`;
  const fullMessage = `${agriculturePrompt} User's message: "${userMessage}"`;

  try {
    const response = await axios.post(
      API_ENDPOINT,
      {
        contents: [
          {
            parts: [
              {
                text: fullMessage,
              },
            ],
          },
        ],
      },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      }
    );

    const reply = response.data.candidates && response.data.candidates[0].content
      ? response.data.candidates[0].content
      : "No valid response from Gemini API.";

    return reply;
  } catch (error) {
    console.error('Error communicating with Gemini API:', error.message);
    return "Failed to get response from Gemini API.";
  }
}

//Route utama buat chatbot
router.post('/', async (req, res) => {
  const userMessage = String(req.body.message || '').trim();
  if (!userMessage) {
    return res.status(400).json({ error: 'Message is required' });
  }

  try {
    const reply = await sendMessageToGemini(userMessage);
    res.json({ reply });
  } catch (error) {
    res.status(500).json({ error: 'Failed to process your request', details: error.message });
  }
});

module.exports = router;
