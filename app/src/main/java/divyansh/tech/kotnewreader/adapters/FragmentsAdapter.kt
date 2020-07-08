package divyansh.tech.kotnewreader.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import divyansh.tech.kotnewreader.ui.fragments.FavouritesFragment
import divyansh.tech.kotnewreader.ui.fragments.tabbedFragments.GeneralFragment

class FragmentsAdapter(val fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    val fragmentList: MutableList<Fragment> = ArrayList<Fragment>()
    val fragmentTitleList: MutableList<String> = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return fragmentList.get(position)
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList.get(position)
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }
}