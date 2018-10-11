package net.teamfruit.androidyoutubedl

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_audioplay.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import net.teamfruit.androidyoutubedl.utils.ExtractURL
import net.teamfruit.androidyoutubedl.utils.MediaPlayerController

class MainActivity : AppCompatActivity() {
    private var audioUrl: String? = null
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
            if (inputUrlBox.text != null) {
                val youtubeUrl = inputUrlBox.text.toString()
                var title: String?
                audioUrl = null
                textView2.text = ""
                job = async(UI) {
                    title = extUrl.getVideoTitle(youtubeUrl)
                    textView3.text = title
                    if (audioUrl != null) launch { mp.reset() }.join()
                    audioUrl = getUrlTask(youtubeUrl)
                    textView2.text = audioUrl
                    mp.setDataSource(audioUrl)
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