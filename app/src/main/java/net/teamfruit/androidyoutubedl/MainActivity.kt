package net.teamfruit.androidyoutubedl

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_audioplay.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var job: Deferred<Unit>? = null
    private var test: Deferred<Unit>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMainScreen()
    }
    private fun setMainScreen() {
        setContentView(R.layout.activity_audioplay)
        val mp = MediaPlayer()
        var dur = 0
        var audioUrl:String? = null
        inputUrlButton.setOnClickListener {
            if (inputUrlBox.text != null) {
                val youtubeUrl = inputUrlBox.text.toString()
                job = async(UI){
                    mp.reset()
                    audioUrl = getUrlTask(youtubeUrl)
                    textView2.text = audioUrl
                    mp.setDataSource(audioUrl)
                    mp.prepare()
                }
            }
        }
        audioButton.setOnClickListener {
            if(audioUrl == null) {
                Toast.makeText(this@MainActivity, "nowloading", Toast.LENGTH_SHORT).show()
            } else {
                when {
                    !mp.isPlaying -> {
                        mp.start()
                    }
                    mp.isPlaying -> {
                        dur = mp.duration
                        mp.pause()
                    }
                }
            }
        }
    }
    private suspend fun getUrlTask(inputURL: String):String { return if(getVideoInfo(inputURL) == "audioURL not found") "audioURL not found" else statusCheck(inputURL) }
}