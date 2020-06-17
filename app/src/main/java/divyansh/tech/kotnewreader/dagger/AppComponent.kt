package divyansh.tech.kotnewreader.dagger

import dagger.Component
import divyansh.tech.kotnewreader.NewsReaderApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ServiceModule::class
])
interface AppComponent {
    fun inject(app: NewsReaderApplication)
}