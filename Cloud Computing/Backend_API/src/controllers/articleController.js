const db = require('../db');

//Fungsi untuk mendapatkan semua artikel
const getAllArticles = async (req, res) => {
  try {
    const articlesRef = db.collection('articles');
    const snapshot = await articlesRef.get();

    if (snapshot.empty) {
      return res.status(404).send('No articles found');
    }

    const articles = [];
    snapshot.forEach(doc => {
      articles.push({ id: doc.id, ...doc.data() });
    });

    res.status(200).json(articles);
  } catch (error) {
    console.error("Error getting articles: ", error);
    res.status(500).send('Error retrieving articles');
  }
};

//Fungsi untuk mendapatkan artikel berdasarkan ID artikel
const getArticleById = async (req, res) => {
  const { id } = req.params;
  try {
    const articleRef = db.collection('articles').doc(id);
    const doc = await articleRef.get();

    if (!doc.exists) {
      return res.status(404).send('Article not found');
    }

    res.status(200).json({ id: doc.id, ...doc.data() });
  } catch (error) {
    console.error("Error getting article: ", error);
    res.status(500).send('Error retrieving article');
  }
};

module.exports = {
  getAllArticles,
  getArticleById
};
