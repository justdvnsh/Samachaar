package divyansh.tech.kotnewreader.ui.fragments.tabbedFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.adapters.NewsAdapter
import kotlinx.android.synthetic.main.fragment_science.*
import javax.inject.Inject

@AndroidEntryPoint
class ScienceFragment: BaseTabFragment() {

    @Inject
    lateinit var newsAdapter: NewsAdapter

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_science, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupRecyclerView()
    }

    override fun provideRecyclerView(): RecyclerView {
        return rvScienceBreakingNews
    }

    override fun provideAdapter(): NewsAdapter {
        return newsAdapter
    }

    override fun provideProgressBar(): ProgressBar {
        return paginationProgressBar
    }

    override fun provideCategory(): String {
        return getString(R.string.science).toLowerCase()
    }
}