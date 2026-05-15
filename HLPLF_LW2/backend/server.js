const express = require('express');
const cors = require('cors');
const app = express();
const PORT = 5000;

app.use(cors());
app.use(express.json());

let articles = [
    { id: 1, title: 'Перша стаття', content: 'Привіт із сервера!', author: 'Admin' }
];

// Отримати всі
app.get('/api/articles', (req, res) => {
    res.json(articles);
});

// Додати нову
app.post('/api/articles', (req, res) => {
    const newArticle = {
        id: Date.now(), // Генерує довге число (Long)
        title: req.body.title,
        content: req.body.content,
        author: req.body.author || "User"
    };
    articles.push(newArticle);
    console.log("Додано:", newArticle);
    res.status(201).json(newArticle);
});

// Видалити
app.delete('/api/articles/:id', (req, res) => {
    const id = parseInt(req.params.id);
    articles = articles.filter(a => a.id !== id);
    console.log("Видалено ID:", id);
    res.status(204).send();
});

app.listen(PORT, () => console.log(`Сервер працює на http://localhost:${PORT}`));