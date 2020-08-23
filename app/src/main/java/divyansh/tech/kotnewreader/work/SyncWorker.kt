package divyansh.tech.kotnewreader.work

import android.content.Context
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.Worker
import androidx.work.WorkerParameters
import divyansh.tech.kotnewreader.database.ArticleDao
import divyansh.tech.kotnewreader.repositories.NewsRepository

class SyncWorker @WorkerInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val newsRepository: NewsRepository,
    private val db: ArticleDao
): Worker(appContext, workerParameters) {

    override fun doWork(): Result {
        val articles = db.getAllArticlesList()
        Log.i("WORK", articles.size.toString())
        try {
            newsRepository.syncArticles(articles)
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "Sync"
    }
}