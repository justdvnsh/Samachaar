package divyansh.tech.kotnewreader.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import divyansh.tech.kotnewreader.database.ArticleDao
import divyansh.tech.kotnewreader.network.models.Article
import divyansh.tech.kotnewreader.repositories.NewsRepository
import javax.inject.Inject

class SyncWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

    @Inject
    lateinit var db: ArticleDao

    override suspend fun doWork(): Result {
        var result: List<Article> = db.getAllArticlesList()
        return try {
//            syncWithRemoteDatabase(result)
            Result.success()
        } catch (e: Exception){
            Result.failure()
        }
    }
}