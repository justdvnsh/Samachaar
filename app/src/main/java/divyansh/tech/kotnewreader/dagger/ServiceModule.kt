package divyansh.tech.kotnewreader.dagger

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ServiceModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder().baseUrl("http://newsapi.org/").addConverterFactory(GsonConverterFactory.create()).build()

}