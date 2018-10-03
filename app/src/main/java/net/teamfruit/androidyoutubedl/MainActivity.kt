package net.teamfruit.androidyoutubedl

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_audioplay.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {
    private var audioUrl:String? = null
    private val mp = MediaPlayer()
    private var job: Deferred<Unit>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMainScreen()
    }

    private fun setMainScreen() {
        setContentView(R.layout.activity_audioplay)

        inputUrlButton.setOnClickListener {
            if (inputUrlBox.text != null) {
                val youtubeUrl = inputUrlBox.text.toString()
                audioUrl = null
                textView2.text = ""
                job = async(UI){
                    if(audioUrl != null) launch { mp.reset() }.join()
                    audioUrl = getUrlTask(youtubeUrl)
                    textView2.text = audioUrl
                    mp.setDataSource(audioUrl)
                    mp.prepare()
                    mp.start()
                    seekBar.max = mp.duration
                    seekBar.progress = mp.currentPosition
                }
            }
        }

        audioButton.setOnClickListener {
            if(audioUrl == null) {
                Toast.makeText(this@MainActivity, "nowloading", Toast.LENGTH_SHORT).show()
            } else {
                when {
                    !mp.isPlaying -> mp.start()
                    mp.isPlaying -> mp.pause()
                }
            }
            currentTime.text = mToS(mp.currentPosition)
            seekBar.progress = mp.currentPosition
        }

        loopButton.setOnClickListener {
            when(mp.isLooping) {
                false -> mp.isLooping = true
                true -> mp.isLooping = false
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mp.seekTo(progress)
                currentTime.text = mToS(mp.currentPosition)
                textView3.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }
    private suspend fun getUrlTask(inputURL: String):String { return if(getVideoInfo(inputURL) == "audioURL not found") "audioURL not found" else statusCheck(inputURL) }

    private fun mToS(ms: Int):String {
        val toSec = ms / 1000
        val min = toSec / 60
        val sec = toSec % 60
        return "$min:$sec"
    }
}