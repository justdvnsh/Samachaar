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
            0 -> return NewsFragment("Business")
            1 -> return NewsFragment("Entertainment")
            2 -> return NewsFragment("Technology")
            3 -> return NewsFragment("Sports")
            4 -> return NewsFragment("Health")
            5 -> return NewsFragment("Science")
            else -> return SearchFragment()
        }
    }

}