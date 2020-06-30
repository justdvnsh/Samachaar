package divyansh.tech.kotnewreader.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import divyansh.tech.kotnewreader.database.ArticleDao
import divyansh.tech.kotnewreader.network.api.NewsApi
import divyansh.tech.kotnewreader.network.models.Article
import divyansh.tech.kotnewreader.network.models.User
import divyansh.tech.kotnewreader.utils.Constants.Companion.USERS
import javax.inject.Inject

class NewsRepository @Inject constructor(
    val db: ArticleDao,
    val api: NewsApi,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    private val usersRef = firestore.collection(USERS)

    fun testIfInjected() {
        Log.i("INJECTED", db.hashCode().toString() + " api ->" + api.hashCode().toString())
    }

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) = api.getBreakingNews(countryCode, pageNumber = pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) = api.searchForNews(searchQuery, pageNumber = pageNumber)

    suspend fun upsertArticle(article: Article) = db.upsert(article)

    fun getAllArticles() = db.getAllArticles()

    suspend fun deleteArticle(article: Article) = db.deleteArticle(article)

}