package divyansh.tech.kotnewreader.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.adapters.NewsAdapter
import divyansh.tech.kotnewreader.ui.viewModels.newsViewModel
import divyansh.tech.kotnewreader.utils.Constants.Companion.SEARCH_TIME_DELAY
import divyansh.tech.kotnewreader.utils.Resource
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    val viewModel: newsViewModel by viewModels()
    @Inject
    lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setupRecyclerView()
        setupObservers()
        setupEditText()
    }

    private fun setupEditText() {
        // set a time delay fot the user to type in the complete query
        var job: Job? = null
        etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_TIME_DELAY)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.getSearchNews(it.toString())
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }
    }

    private fun showProgress() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        paginationProgressBar.visibility = View.GONE
    }

    private fun  setupObservers() {
        viewModel.searchNews.observe(this@SearchActivity, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgress()
                    it.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                        Toast.makeText(this@SearchActivity, "Success ${it.articles.size.toString()}", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Error -> {
                    hideProgress()
                    Log.i("Search", it.message.toString())
                    it.message?.let {
                        Toast.makeText(this@SearchActivity, "Failed ${it}", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    showProgress()
                }
            }
        })
    }
}