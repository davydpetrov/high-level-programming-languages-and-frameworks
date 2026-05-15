// frontend/src/App.jsx
import { useState, useEffect } from 'react';
import './App.css';

function App() {
    const [articles, setArticles] = useState([]);
    const [loading, setLoading] = useState(true);

    // Використовуємо useEffect для завантаження даних при монтуванні компонента
    useEffect(() => {
        fetch('http://localhost:5000/api/articles')
            .then((response) => response.json())
            .then((data) => {
                setArticles(data);
                setLoading(false);
            })
            .catch((error) => {
                console.error('Помилка завантаження даних:', error);
                setLoading(false);
            });
    }, []);

    return (
        <div className="container">
            <header>
                <h1>Блог розробника</h1>
            </header>

            <main>
                {loading ? (
                    <p>Завантаження статей...</p>
                ) : (
                    <div className="articles-list">
                        {articles.map((article) => (
                            <article key={article.id} className="article-card">
                                <h2>{article.title}</h2>
                                <div className="author">Автор: {article.author}</div>
                                <p className="content">{article.content}</p>
                            </article>
                        ))}
                    </div>
                )}
            </main>
        </div>
    );
}

export default App;