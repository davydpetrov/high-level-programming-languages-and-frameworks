package com.example.hlplf_lw3 // ПЕРЕВІРТЕ СВІЙ ПАКЕТ!

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// 1. Модель даних з типом Long
data class Article(
    val id: Long,
    val title: String,
    val content: String,
    val author: String
)

// 2. Інтерфейс API
interface BlogApi {
    @GET("api/articles")
    suspend fun getArticles(): List<Article>

    @POST("api/articles")
    suspend fun addArticle(@Body article: Article): Article

    @DELETE("api/articles/{id}")
    suspend fun deleteArticle(@Path("id") id: Long): retrofit2.Response<Unit>
}

class MainActivity : AppCompatActivity() {

    private lateinit var api: BlogApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Елементи UI
        val btnRefresh = findViewById<Button>(R.id.btnRefresh)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val editTitle = findViewById<EditText>(R.id.editTitle)
        val editContent = findViewById<EditText>(R.id.editContent)
        val container = findViewById<LinearLayout>(R.id.articlesContainer)

        // Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(BlogApi::class.java)

        // Кнопка оновити
        btnRefresh.setOnClickListener { loadArticles(container) }

        // Кнопка додати
        btnAdd.setOnClickListener {
            val title = editTitle.text.toString()
            val content = editContent.text.toString()

            if (title.isNotBlank() && content.isNotBlank()) {
                val newArticle = Article(0L, title, content, "Student")
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        api.addArticle(newArticle)
                        withContext(Dispatchers.Main) {
                            editTitle.text.clear()
                            editContent.text.clear()
                            loadArticles(container)
                            Toast.makeText(this@MainActivity, "Додано!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        showError(e)
                    }
                }
            }
        }

        // Завантажити при старті
        loadArticles(container)
    }

    private fun loadArticles(container: LinearLayout) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val articles = api.getArticles()
                withContext(Dispatchers.Main) {
                    container.removeAllViews()
                    articles.forEach { article ->
                        val textView = TextView(this@MainActivity)
                        textView.text = "📌 ${article.title}\n${article.content}\n[Видалити]\n---"
                        textView.setPadding(0, 20, 0, 20)
                        textView.setOnClickListener { deleteArticle(article.id, container) }
                        container.addView(textView)
                    }
                }
            } catch (e: Exception) {
                showError(e)
            }
        }
    }

    private fun deleteArticle(id: Long, container: LinearLayout) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.deleteArticle(id)
                withContext(Dispatchers.Main) {
                    loadArticles(container)
                    Toast.makeText(this@MainActivity, "Видалено", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                showError(e)
            }
        }
    }

    private suspend fun showError(e: Exception) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@MainActivity, "Помилка: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}