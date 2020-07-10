package divyansh.tech.kotnewreader.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.adapters.FragmentsAdapter
import divyansh.tech.kotnewreader.adapters.NewsAdapter
import divyansh.tech.kotnewreader.ui.NewsActivity
import divyansh.tech.kotnewreader.ui.fragments.tabbedFragments.*
import divyansh.tech.kotnewreader.utils.Constants.Companion.QUERY_PAGE_SIZE
import divyansh.tech.kotnewreader.utils.Resource
import kotlinx.android.synthetic.main.common_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import javax.inject.Inject

class BreakingNewsFragment : BaseFragment() {

    lateinit var fragmentAdapter: FragmentsAdapter

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_breaking_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.titleText.text = "Breaking News"
        setStatePagerAdapter()
    }

    private fun setStatePagerAdapter() {
        fragmentAdapter = FragmentsAdapter(this)
        pager.adapter = fragmentAdapter
        TabLayoutMediator(tabs, pager) {tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.business)
                1 -> tab.text = getString(R.string.entertainment)
                2 -> tab.text = getString(R.string.tech)
                3 -> tab.text = getString(R.string.sports)
                4 -> tab.text = getString(R.string.health)
                5 -> tab.text = getString(R.string.science)
            }
        }.attach()
        pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
//                TODO("Not yet implemented")
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (shouldPaginate) viewModel.getBreakingNews("in", tabs.getTabAt(position)?.text.toString().toLowerCase())
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        viewModel.pageChanged = true
                        viewModel.getBreakingNews("in", getString(R.string.business))
                    }
                    1 -> {
                        viewModel.pageChanged = true
                        viewModel.getBreakingNews("in", getString(R.string.entertainment))
                    }
                    2 -> {
                        viewModel.pageChanged = true
                        viewModel.getBreakingNews("in", getString(R.string.tech))
                    }
                    3 -> {
                        viewModel.pageChanged = true
                        viewModel.getBreakingNews("in", getString(R.string.sports))
                    }
                    4 -> {
                        viewModel.pageChanged = true
                        viewModel.getBreakingNews("in", getString(R.string.health))
                    }
                    5 -> {
                        viewModel.pageChanged = true
                        viewModel.getBreakingNews("in", getString(R.string.science))
                    }
                    else -> {
                        viewModel.pageChanged = false
                        SearchFragment()
                    }
                }
            }

        })
    }

}