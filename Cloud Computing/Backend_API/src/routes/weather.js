const express = require('express');
const router = express.Router();
const axios = require('axios');

const OPENWEATHER_API_KEY = process.env.CUACA_API_KEY;

router.get('/forecast', async (req, res) => {
    const { lat, lon } = req.query;

    if (!lat || !lon) {
        return res.status(400).json({ error: 'Latitude dan Longitude harus diisi!' });
    }

    try {
        //Panggil OpenWeather API untuk data perkiraan cuaca
        const response = await axios.get(`https://api.openweathermap.org/data/2.5/forecast`, {
            params: {
                lat,
                lon,
                appid: OPENWEATHER_API_KEY,
                units: 'metric',
            },
        });

    
        const city = response.data.city.name;
        const country = response.data.city.country;

     
        const data = response.data.list;

        //Fungsi untuk mengelompokkan data berdasarkan hari
        const groupedForecast = data.reduce((acc, item) => {
            const date = item.dt_txt.split(' ')[0]; 
            if (!acc[date]) {
                acc[date] = [];
            }
            acc[date].push({
                waktu: item.dt_txt,
                suhu: `${item.main.temp} °C`,
                kelembapan: `${item.main.humidity}%`,
                deskripsi: item.weather[0].description,
            });
            return acc;
        }, {});

        //Ambil data untuk hari ini, besok, dan lusa
        const today = new Date();
        const result = [];

        for (let i = 0; i < 3; i++) {
            const targetDate = new Date(today);
            targetDate.setDate(today.getDate() + i);

            const dateString = targetDate.toISOString().split('T')[0];
            if (groupedForecast[dateString]) {
                result.push({
                    tanggal: dateString,
                    ramalan: groupedForecast[dateString],
                });
            }
        }

        res.json({
            kota: `${city}, ${country}`, 
            ramalan: result, 
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

module.exports = router;
