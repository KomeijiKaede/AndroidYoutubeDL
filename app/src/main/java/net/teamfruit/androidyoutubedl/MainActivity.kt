package net.teamfruit.androidyoutubedl

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import net.teamfruit.androidyoutubedl.ui.AudioPlayerFragment
import net.teamfruit.androidyoutubedl.ui.InputURLFragment

class MainActivity : AppCompatActivity() {
    companion object { fun newInstance(): MainActivity { return MainActivity() } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplay)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.controller, AudioPlayerFragment.newInstance())
                    .add(R.id.urlInput, InputURLFragment.newInstance())
                    .commit()
        }

    }
}