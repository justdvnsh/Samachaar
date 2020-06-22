package divyansh.tech.kotnewreader.dagger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import divyansh.tech.kotnewreader.adapters.NewsAdapter

@Module
@InstallIn(FragmentComponent::class)
object BreakingNewsModule {

    @Provides
    fun provideAdapter(): NewsAdapter = NewsAdapter()
}