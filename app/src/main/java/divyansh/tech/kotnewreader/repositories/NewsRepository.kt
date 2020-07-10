package divyansh.tech.kotnewreader.repositories

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import divyansh.tech.kotnewreader.database.ArticleDao
import divyansh.tech.kotnewreader.network.api.NewsApi
import divyansh.tech.kotnewreader.network.models.Article
import divyansh.tech.kotnewreader.network.models.User
import divyansh.tech.kotnewreader.utils.Constants.Companion.USERS
import org.w3c.dom.Document
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

    suspend fun getBreakingNews(countryCode: String, category: String, pageNumber: Int) = api.getBreakingNews(countryCode, category = category, pageNumber = pageNumber)

    suspend fun searchNews(searchQuery: String) = api.searchForNews(searchQuery)

    suspend fun upsertArticle(article: Article) = db.upsert(article)

    fun getAllArticles() = db.getAllArticles()

    suspend fun deleteArticle(article: Article) = db.deleteArticle(article)

    fun detectImage(image: Bitmap): MutableLiveData<String>? {
        val recogText: MutableLiveData<String>? = MutableLiveData()
        val firebaseImage: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(image)
        val textRecognizer: FirebaseVisionTextRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
        textRecognizer.processImage(firebaseImage).addOnCompleteListener {
            if (it.isSuccessful) {
                recogText?.value = it.result?.text
            } else {
                recogText?.value = null
                Log.i("NEWSREPO", it.exception?.message)
            }
        }
        return recogText
    }

    fun syncArticles(articles: List<Article>) {
        val currentUser: FirebaseUser? = firebaseAuth.currentUser
        val emailRef: DocumentReference = usersRef.document(currentUser?.email!!)
        Log.i("NEWSREPO", articles.size.toString())
        emailRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val document = it.result
                if (document?.exists()!!) {
                    val user: User = User(
                        email = currentUser.email,
                        uid = currentUser.uid,
                        name = currentUser.displayName,
                        isAuthenticated = true,
                        isNew = false,
                        isCreated = true,
                        articles = articles
                    )
                    emailRef.set(user).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i("NEWSREPO", "SYNCCED SUCCESS")
                        } else {
                            Log.i("NEWSREPO", task.exception?.message)
                        }
                    }
                } else {
                    Log.i("NEWSREPO", it.exception?.message)
                }
            } else {
                Log.i("NEWSREPO", "SOMETHING WENT WRONG")
            }
        }
    }
}