package divyansh.tech.kotnewreader.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.adapters.FragmentsAdapter
import kotlinx.android.synthetic.main.common_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment : BaseFragment() {

    private lateinit var fragmentAdapter: FragmentsAdapter

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_breaking_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.titleText.text = getString(R.string.BreakingNewsTitle)
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
                if (shouldPaginate) viewModel.getBreakingNews(getString(R.string.countryCode), tabs.getTabAt(position)?.text.toString().toLowerCase())
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        viewModel.pageChanged = true
                        viewModel.getBreakingNews(getString(R.string.countryCode), getString(R.string.business))
                    }
                    1 -> {
                        viewModel.pageChanged = true
                        viewModel.getBreakingNews(getString(R.string.countryCode), getString(R.string.entertainment))
                    }
                    2 -> {
                        viewModel.pageChanged = true
                        viewModel.getBreakingNews(getString(R.string.countryCode), getString(R.string.tech))
                    }
                    3 -> {
                        viewModel.pageChanged = true
                        viewModel.getBreakingNews(getString(R.string.countryCode), getString(R.string.sports))
                    }
                    4 -> {
                        viewModel.pageChanged = true
                        viewModel.getBreakingNews(getString(R.string.countryCode), getString(R.string.health))
                    }
                    5 -> {
                        viewModel.pageChanged = true
                        viewModel.getBreakingNews(getString(R.string.countryCode), getString(R.string.science))
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