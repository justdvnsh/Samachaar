package divyansh.tech.kotnewreader.database

import androidx.lifecycle.LiveData
import androidx.room.*
import divyansh.tech.kotnewreader.network.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}