package net.teamfruit.androidyoutubedl

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_mainmenu.*
import net.teamfruit.androidyoutubedl.ui.*

class MainActivity : AppCompatActivity() {
    companion object { fun newInstance(): MainActivity { return MainActivity() } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)

        if (savedInstanceState == null) { supportFragmentManager.beginTransaction().add(R.id.controller, AudioPlayerFragment.newInstance()).commit() }

        viewPager.adapter = TabPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }
}