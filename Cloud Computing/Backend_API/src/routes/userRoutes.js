const express = require('express');
const { registerUser, loginUser, deleteUser, updateProfile, resetPassword, requestVerificationCode } = require('../controllers/authController');
const { verifyEmail } = require('../controllers/emailController');

const router = express.Router();

router.post('/register', registerUser);
router.post('/login', loginUser);
router.post('/verify-email', verifyEmail);
router.put('/update-profile/:username', updateProfile);
router.post('/reset-password', resetPassword);
router.post('/request-verification-code', requestVerificationCode);
router.delete('/delete/:username', deleteUser);

module.exports = router;
