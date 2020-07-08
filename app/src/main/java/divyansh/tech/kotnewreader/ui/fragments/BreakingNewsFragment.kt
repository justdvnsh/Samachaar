package divyansh.tech.kotnewreader.ui.fragments

import android.os.Bundle
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
import com.google.android.material.tabs.TabLayout
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

@AndroidEntryPoint
class BreakingNewsFragment : BaseFragment() {

    @Inject
    lateinit var newsAdapter: NewsAdapter
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
        setupTabLayoutListener()
    }

    private fun setupTabLayoutListener() {
        tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
                val fm = childFragmentManager
                val ft = fm.beginTransaction()
                val count = fm.backStackEntryCount
                if (count >= 1) fm.popBackStack()
                ft.commit()
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setStatePagerAdapter() {
        fragmentAdapter = FragmentsAdapter(childFragmentManager)
        fragmentAdapter.apply {
            addFragment(GeneralFragment(), getString(R.string.general))
            addFragment(BusinessFragment(), getString(R.string.business))
            addFragment(EntertainmentFragment(), getString(R.string.entertainment))
            addFragment(TechFragment(), getString(R.string.tech))
            addFragment(SportsFragment(), getString(R.string.sports))
            addFragment(HealthFragment(), getString(R.string.health))
            addFragment(ScienceFragment(), getString(R.string.science))
        }
        pager.adapter = fragmentAdapter
        tabs.setupWithViewPager(pager, true)
    }
}