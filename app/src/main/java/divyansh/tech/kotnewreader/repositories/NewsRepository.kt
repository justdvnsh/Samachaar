package divyansh.tech.kotnewreader.repositories

import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.database.ArticleDao
import divyansh.tech.kotnewreader.database.ArticleDatabase
import divyansh.tech.kotnewreader.network.api.NewsApi
import divyansh.tech.kotnewreader.network.models.Article
import javax.inject.Inject

class NewsRepository @Inject constructor(
    val db: ArticleDao,
    val api: NewsApi
) {

    fun testIfInjected() {
        Log.i("INJECTED", db.hashCode().toString() + " api ->" + api.hashCode().toString())
    }

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) = api.getBreakingNews(countryCode, pageNumber = pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) = api.searchForNews(searchQuery, pageNumber = pageNumber)

    suspend fun upsertArticle(article: Article) = db.upsert(article)

    fun getAllArticles() = db.getAllArticles()

    suspend fun deleteArticle(article: Article) = db.deleteArticle(article)
}