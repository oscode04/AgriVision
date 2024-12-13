const express = require("express");
const { getAllArticles, getArticleById } = require("../controllers/articleController");
const router = express.Router();

//semua artikel
router.get("/all", getAllArticles);

//artikel berdasarkan id
router.get("/:id", getArticleById);

module.exports = router;
