package divyansh.tech.kotnewreader.dagger

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import divyansh.tech.kotnewreader.adapters.NewsAdapter
import divyansh.tech.kotnewreader.database.ArticleDatabase
import divyansh.tech.kotnewreader.network.api.NewsApi
import divyansh.tech.kotnewreader.utils.Constants.Companion.ARTICLE_DATABASE
import divyansh.tech.kotnewreader.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideArticleDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        ArticleDatabase::class.java,
        ARTICLE_DATABASE
    ).build()

    @Singleton
    @Provides
    fun providesArticleDao(db: ArticleDatabase) = db.getArticleDao()

    @Singleton
    @Provides
    fun provideRetrofit() = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

    @Singleton
    @Provides
    fun provideNewsApi(retrofit: Retrofit) = retrofit.create(NewsApi::class.java)

    @Provides
    fun provideAdapter(): NewsAdapter = NewsAdapter()
}