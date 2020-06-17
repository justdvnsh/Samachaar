package divyansh.tech.kotnewreader.dagger

import androidx.fragment.app.Fragment
import dagger.Component
import divyansh.tech.kotnewreader.database.ArticleDao
import divyansh.tech.kotnewreader.network.api.NewsApi
import divyansh.tech.kotnewreader.ui.NewsActivity
import divyansh.tech.kotnewreader.ui.fragments.BreakingNewsFragment
import divyansh.tech.kotnewreader.ui.fragments.FavouritesFragment

@Component(
    modules = [NewsModule::class]
)
interface NewsComponent {
    fun inject(activity: NewsActivity)
}