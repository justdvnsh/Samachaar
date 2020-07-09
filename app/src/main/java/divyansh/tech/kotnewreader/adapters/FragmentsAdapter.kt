package divyansh.tech.kotnewreader.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import divyansh.tech.kotnewreader.ui.fragments.FavouritesFragment
import divyansh.tech.kotnewreader.ui.fragments.SearchFragment
import divyansh.tech.kotnewreader.ui.fragments.tabbedFragments.*

class FragmentsAdapter(val fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 6
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return BusinessFragment()
            1 -> return EntertainmentFragment()
            2 -> return TechFragment()
            3 -> return SportsFragment()
            4 -> return HealthFragment()
            5 -> return ScienceFragment()
            else -> return SearchFragment()
        }
    }

}