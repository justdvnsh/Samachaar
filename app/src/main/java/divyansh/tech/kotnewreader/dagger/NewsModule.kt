package divyansh.tech.kotnewreader.dagger

import dagger.Module
import dagger.Provides
import divyansh.tech.kotnewreader.database.ArticleDao
import divyansh.tech.kotnewreader.database.ArticleDatabase
import divyansh.tech.kotnewreader.network.api.NewsApi
import divyansh.tech.kotnewreader.network.models.Article
import retrofit2.Retrofit

@Module
class NewsModule {

    @Provides
    fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)

    @Provides
    fun provideArticleDao(db: ArticleDatabase): ArticleDao = db.getArticleDao()
}