package divyansh.tech.kotnewreader.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.adapters.NewsAdapter
import divyansh.tech.kotnewreader.ui.NewsActivity
import divyansh.tech.kotnewreader.utils.Resource
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment: BaseFragment() {

    @Inject
    lateinit var newsAdapter: NewsAdapter

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleText?.text = getString(R.string.homeTitle)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(getString(R.string.articleArgument), it)
            }
            findNavController().navigate(
                R.id.action_homeFragment2_to_articleFragment,
                bundle
            )
        }
        rvHomeNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun  setupObservers() {
        (activity as NewsActivity).city?.let {
            viewModel.getSearchNews(it)
            viewModel.getDailyReports()
        }
        viewModel.coronaDetails.observe(viewLifecycleOwner, Observer {it ->
            when (it) {
                is Resource.Success -> {
                    alert.dismiss()
                    it.data?.let {
                        totalCases.text = it.totalCases.toString()
                        activeCases.text = it.activeCases.toString()
                        totalRecoveries.text = it.recovered.toString()
                        totalDeaths.text = it.deaths.toString()
                    }
                }

                is Resource.Error -> {
                    alert.dismiss()
                    it.message?.let {
                        Toast.makeText(activity, "${getString(R.string.failed)} ${it}", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    alert.show()
                }
            }
        })
        viewModel.searchNews.observe(viewLifecycleOwner, Observer {it ->
            when (it) {
                is Resource.Success -> {
                    alert.dismiss()
                    it.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                    }
                }

                is Resource.Error -> {
                    alert.dismiss()
                    it.message?.let {
                        Toast.makeText(activity, "${getString(R.string.failed)} ${it}", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    alert.show()
                }
            }
        })
    }
}