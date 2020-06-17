package divyansh.tech.kotnewreader

import android.app.Application
import divyansh.tech.kotnewreader.dagger.AppComponent
import divyansh.tech.kotnewreader.dagger.AppModule
import divyansh.tech.kotnewreader.dagger.DaggerAppComponent

class NewsReaderApplication: Application() {

    private val newsReaderComponent: AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()
        newsReaderComponent.inject(this)
    }
}