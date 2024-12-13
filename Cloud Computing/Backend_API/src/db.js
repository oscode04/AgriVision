const admin = require('firebase-admin');
require('dotenv').config();

//Inisialisasi firestore dengan kredensial service account
const serviceAccount = require('./path to your service account.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();
console.log("Firebase initialized and connected");


module.exports = db;
