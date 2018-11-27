package net.teamfruit.androidyoutubedl.ui

import android.support.v4.app.FragmentManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import net.teamfruit.androidyoutubedl.experimental.RecyclerListFragment
import net.teamfruit.androidyoutubedl.experimental.ExtractFragment

class TabPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> RecyclerListFragment.newInstance()
            1 -> ExtractFragment.newInstance()
            2 -> RecyclerListFragment.newInstance()
            else -> RecyclerListFragment.newInstance()
        }
    }

    override fun getCount(): Int {return 3}
}