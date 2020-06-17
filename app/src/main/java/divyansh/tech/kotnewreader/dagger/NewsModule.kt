package divyansh.tech.kotnewreader.dagger

import dagger.Module
import dagger.Provides
import divyansh.tech.kotnewreader.network.api.NewsApi
import retrofit2.Retrofit

@Module
class NewsModule {

    @Provides
    fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)
}