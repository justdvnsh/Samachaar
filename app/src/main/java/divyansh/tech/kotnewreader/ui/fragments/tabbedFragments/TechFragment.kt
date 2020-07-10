package divyansh.tech.kotnewreader.ui.fragments.tabbedFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.adapters.NewsAdapter
import divyansh.tech.kotnewreader.ui.fragments.BaseFragment
import divyansh.tech.kotnewreader.utils.Resource
import kotlinx.android.synthetic.main.fragment_tech.*
import javax.inject.Inject


class TechFragment: BaseFragment() {

    lateinit var newsAdapter: NewsAdapter

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_tech, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }
        rvTechBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
//            addOnScrollListener(scrollListener)
        }
    }

    fun setupObservers() {
        viewModel.getBreakingNews("in", getString(R.string.tech))
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgress(paginationProgressBar)
                    it.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                    }
                }

                is Resource.Error -> {
                    hideProgress(paginationProgressBar)
                    it.message?.let {
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    showProgress(paginationProgressBar)
                }
            }
        })
    }


    override fun provideCategory(): String {
        return ""
    }
}