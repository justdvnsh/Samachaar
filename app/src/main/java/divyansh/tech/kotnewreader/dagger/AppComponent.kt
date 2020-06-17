package divyansh.tech.kotnewreader.dagger

import android.app.Application
import dagger.Component
import divyansh.tech.kotnewreader.NewsReaderApplication
import divyansh.tech.kotnewreader.database.ArticleDatabase
import divyansh.tech.kotnewreader.ui.NewsActivity
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ServiceModule::class
])
interface AppComponent {
    fun inject(app: NewsReaderApplication)
}