package divyansh.tech.kotnewreader.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import divyansh.tech.kotnewreader.models.Article

@Database(
    entities = [Article::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase: RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao
}