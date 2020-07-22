package divyansh.tech.kotnewreader.dagger

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.adapters.FragmentsAdapter
import divyansh.tech.kotnewreader.adapters.KeyPhrasesAdapter
import divyansh.tech.kotnewreader.adapters.NewsAdapter
import divyansh.tech.kotnewreader.database.ArticleDatabase
import divyansh.tech.kotnewreader.network.api.CoronaApi
import divyansh.tech.kotnewreader.network.api.MLApi
import divyansh.tech.kotnewreader.network.api.NewsApi
import divyansh.tech.kotnewreader.utils.Constants.Companion.ARTICLE_DATABASE
import divyansh.tech.kotnewreader.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.security.Key
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
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun providesArticleDao(db: ArticleDatabase) = db.getArticleDao()

    @Singleton
    @Provides
    fun provideRetrofit() = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

    @Singleton
    @Provides
    fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)

    @Singleton
    @Provides
    fun provideCoronaApi(retrofit: Retrofit): CoronaApi = retrofit.create(CoronaApi::class.java)

    @Singleton
    @Provides
    fun provideMLApi(retrofit: Retrofit): MLApi = retrofit.create(MLApi::class.java)

    @Provides
    fun provideAdapter(): NewsAdapter = NewsAdapter()

    @Provides
    fun provideAdapterKeyPhrases(): KeyPhrasesAdapter = KeyPhrasesAdapter()

    @Provides
    @Singleton
    fun provideFirebaseAuthClient() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

}