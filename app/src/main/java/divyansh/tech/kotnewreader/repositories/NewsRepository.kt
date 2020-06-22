package divyansh.tech.kotnewreader.repositories

import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.database.ArticleDao
import divyansh.tech.kotnewreader.database.ArticleDatabase
import divyansh.tech.kotnewreader.network.api.NewsApi
import javax.inject.Inject

class NewsRepository @Inject constructor(
    val db: ArticleDao,
    val api: NewsApi
) {

    fun testIfInjected() {
        Log.i("INJECTED", db.hashCode().toString() + " api ->" + api.hashCode().toString())
    }
}