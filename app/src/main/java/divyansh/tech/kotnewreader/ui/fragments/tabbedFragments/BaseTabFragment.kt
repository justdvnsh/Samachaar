package divyansh.tech.kotnewreader.ui.fragments.tabbedFragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.adapters.NewsAdapter
import divyansh.tech.kotnewreader.ui.fragments.BaseFragment
import divyansh.tech.kotnewreader.utils.Constants
import divyansh.tech.kotnewreader.utils.Resource

abstract class BaseTabFragment : BaseFragment(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    fun setupRecyclerView() {
        provideAdapter().setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }
        provideRecyclerView().apply {
            adapter = provideAdapter()
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(scrollListener)
        }
    }

    fun setupObservers() {
        viewModel.getBreakingNews("in", provideCategory())
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgress(provideProgressBar())
                    it.data?.let {
                        provideAdapter().differ.submitList(it.articles.toList())
                        val totalPages = it.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingPageNumber == totalPages
                    }
                }

                is Resource.Error -> {
                    hideProgress(provideProgressBar())
                    it.message?.let {
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    showProgress(provideProgressBar())
                }
            }
        })
    }

    abstract fun provideRecyclerView(): RecyclerView
    abstract fun provideAdapter(): NewsAdapter
    abstract fun provideProgressBar(): ProgressBar
}