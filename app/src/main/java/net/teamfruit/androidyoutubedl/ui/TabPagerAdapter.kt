package net.teamfruit.androidyoutubedl.ui

import android.support.v4.app.FragmentManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter

class TabPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> MusicListFragment.newInstance()
            1 -> InputURLFragment.newInstance()//DBControllerFragment.newInstance()
            else -> MusicListFragment.newInstance()
        }
    }

    override fun getCount(): Int {return 2}
}