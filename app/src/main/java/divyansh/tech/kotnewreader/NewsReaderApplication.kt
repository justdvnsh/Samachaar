package divyansh.tech.kotnewreader

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsReaderApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}