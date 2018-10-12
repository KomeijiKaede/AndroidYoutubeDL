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
    private val mp = MediaPlayerController.mp
    private var job: Deferred<Unit>? = null
    private val extUrl = ExtractURL.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMainScreen()
    }

    private fun setMainScreen() {
        setContentView(R.layout.activity_audioplay)
        inputUrlButton.setOnClickListener {
            val youtubeURL:String
            var audioURL:String? = null
            if(inputUrlBox.text != null) {
                youtubeURL = inputUrlBox.text.toString()
                job = async(UI) {
                    if(audioURL != null) { launch {
                        mp.reset()
                        AudioPlayerFragment.newInstance().initSeekBar()
                    }.join()
                    }
                    audioURL = getUrlTask(youtubeURL)
                    textView3.text = extUrl.getVideoTitle(youtubeURL)
                    textView2.text = audioURL
                    mp.setDataSource(audioURL)
                    mp.prepare()
                    mp.start()
                }
            }
        }
    }

    private suspend fun getUrlTask(inputURL: String): String {
        return if (extUrl.getVideoInfo(inputURL) == "audioURL not found") "audioURL not found" else extUrl.statusCheck(inputURL)
    }
}