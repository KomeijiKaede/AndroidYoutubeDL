package net.teamfruit.androidyoutubedl

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_audioplay.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {
    //private var countDownTimer: CountDownTimer? = null
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
                job = async(UI){
                    launch { mp.reset() }.join()
                    audioUrl = getUrlTask(youtubeUrl)
                    textView2.text = audioUrl
                    mp.setDataSource(audioUrl)
                    mp.prepare()
                    mp.start()
                    seekBar.progress = Math.ceil(mp.duration * mp.currentPosition / 100.0).toInt()
                    currentTime.text = durationToSec(mp.duration)
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
        }

        loopButton.setOnClickListener {
            when(mp.isLooping) {
                false -> mp.isLooping = true
                true -> mp.isLooping = false
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(audioUrl != null) {

                } else {}
            }
        })
        seekBar.setOnTouchListener {_, event ->
            if(event?.action == MotionEvent.ACTION_UP) {
                val progress = seekBar.progress
                textView3.text = progress.toString()
            } else {}
            return@setOnTouchListener false
        }
    }
    private suspend fun getUrlTask(inputURL: String):String { return if(getVideoInfo(inputURL) == "audioURL not found") "audioURL not found" else statusCheck(inputURL) }

    private fun durationToSec(sec :Int):String {
        val toSec = sec / 1000
        val m = toSec / 60
        val s = toSec % 60
        return "$m:$s"
    }
}