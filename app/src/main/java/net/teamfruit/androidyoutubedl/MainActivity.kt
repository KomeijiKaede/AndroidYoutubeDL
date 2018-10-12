package net.teamfruit.androidyoutubedl

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_audioplay.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import net.teamfruit.androidyoutubedl.ui.AudioPlayerFragment
import net.teamfruit.androidyoutubedl.utils.ExtractURL
import net.teamfruit.androidyoutubedl.utils.MediaPlayerController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMainScreen()
    }

    private fun setMainScreen() {
        setContentView(R.layout.activity_audioplay)
    }
}