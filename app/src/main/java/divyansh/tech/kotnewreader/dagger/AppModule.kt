package divyansh.tech.kotnewreader.dagger

import android.app.Application
import android.content.Context
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = application
}