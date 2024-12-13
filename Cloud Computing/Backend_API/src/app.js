const express = require('express');
require('dotenv').config();

const app = express();
app.use(express.json());

app.get('/', (req, res) => {
    res.send(`
      <!DOCTYPE html>
      <html lang="en">
      <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>Welcome to Agrivision API</title>
          <style>
              body {
                  font-family: Arial, sans-serif;
                  background-color: #f4f4f9;
                  display: flex;
                  justify-content: center;
                  align-items: center;
                  height: 100vh;
                  margin: 0;
              }
              .container {
                  text-align: center;
                  padding: 30px;
                  border-radius: 10px;
                  background-color: #fff;
                  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
              }
              h1 {
                  color: #468a48;
              }
              p {
                  font-size: 18px;
                  color: #555;
              }
              .footer {
                  margin-top: 20px;
                  font-size: 14px;
                  color: #888;
              }
          </style>
      </head>
      <body>
          <div class="container">
              <h1>WELCOME TO AGRIVISION APP API BY CLOUD COMPUTING TEAM C242-PS414</h1>
              <p>Your trusted partner in agricultural solutions.</p>
              <p>Postman API documentation in this link <a href="https://drive.google.com/drive/folders/17dfoTE1rOJIpTSEmBGvWybYUE-ZMEn7G?usp=sharing" target="_blank">AGRIVISION API DOCUMENTATION</a>.</p>
              <div class="footer">Powered by Agrivision</div>
          </div>
          
      </body>
      </html>
      `);
  });

//Routes yang dibuat
app.use('/weather', require('./routes/weather'));
app.use('/weather-city', require('./routes/weathercity'));
app.use('/articles', require('./routes/articleRoutes'));
app.use('/users', require('./routes/userRoutes'));
app.use('/chatbot', require('./routes/chatbot')); 

module.exports = app;
