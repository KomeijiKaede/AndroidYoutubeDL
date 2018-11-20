package net.teamfruit.androidyoutubedl.ui

import android.support.v4.app.FragmentManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import net.teamfruit.androidyoutubedl.experimental.ExtractFragment
import net.teamfruit.androidyoutubedl.experimental.RecyclerViewFragment

class TabPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> RecyclerViewFragment.newInstance()
            1 -> ExtractFragment.newInstance()
            else -> RecyclerViewFragment.newInstance()
        }
    }

    override fun getCount(): Int {return 2}
}