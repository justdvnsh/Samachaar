package divyansh.tech.kotnewreader

import android.app.Application
import divyansh.tech.kotnewreader.dagger.AppComponent
import divyansh.tech.kotnewreader.dagger.AppModule
import divyansh.tech.kotnewreader.dagger.DaggerAppComponent

class NewsReaderApplication: Application() {

    lateinit var newsReaderComponent: AppComponent

    private fun initDagger(app: NewsReaderApplication): AppComponent =
        DaggerAppComponent.builder().appModule(AppModule(app)).build()
}