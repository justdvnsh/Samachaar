package divyansh.tech.kotnewreader.dagger

import android.app.Application
import dagger.Module
import dagger.Provides
import divyansh.tech.kotnewreader.database.ArticleDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ServiceModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder().baseUrl("http://newsapi.org/").addConverterFactory(GsonConverterFactory.create()).build()

    @Provides
    @Singleton
    fun provideArticleDatabase(application: Application): ArticleDatabase = ArticleDatabase.invoke(application)
}