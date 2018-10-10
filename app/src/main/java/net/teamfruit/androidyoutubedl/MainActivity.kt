package net.teamfruit.androidyoutubedl

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_audioplay.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {
    private var audioUrl: String? = null
    private val mp = MediaPlayer()
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private var job: Deferred<Unit>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setMainScreen()
    }

    private fun setMainScreen() {
        setContentView(R.layout.activity_audioplay)

        inputUrlButton.setOnClickListener {
            if (inputUrlBox.text != null) {
                val youtubeUrl = inputUrlBox.text.toString()
                audioUrl = null
                textView2.text = ""
                job = async(UI) {
                    textView3.text = getVideoTitle(youtubeUrl)
                    if (audioUrl != null) launch { mp.reset() }.join()
                    audioUrl = getUrlTask(youtubeUrl)
                    notification()
                    textView2.text = audioUrl
                    mp.setDataSource(audioUrl)
                    mp.prepare()
                    mp.start()
                    initSeekBar()
                }
            }
        }

        audioButton.setOnClickListener {
            if (audioUrl == null) {
                Toast.makeText(this@MainActivity, "nowloading", Toast.LENGTH_SHORT).show()
            } else {
                when {
                    !mp.isPlaying -> {
                        mp.start()
                        audioButton.setBackgroundResource(R.drawable.ic_pause_black_24dp)
                    }
                    mp.isPlaying -> {
                        mp.pause()
                        audioButton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp)
                    }
                }
            }
            currentTime.text = MediaPlayerController.mToS(mp.currentPosition)
            seekBar.progress = mp.currentPosition
        }

        loopButton.setOnClickListener {
            when (mp.isLooping) {
                false -> mp.isLooping = true
                true -> mp.isLooping = false
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mp.seekTo(progress)
                    currentTime.text = MediaPlayerController.mToS(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private suspend fun getUrlTask(inputURL: String): String {
        return if (getVideoInfo(inputURL) == "audioURL not found") "audioURL not found" else statusCheck(inputURL)
    }

    private fun mToS(ms: Int): String {
        val toSec = ms / 1000
        val min = toSec / 60
        val sec = toSec % 60
        return "$min:$sec"
    }

    private fun initSeekBar() {
        seekBar.max = mp.duration

        runnable = Runnable {
            seekBar.progress = mp.currentPosition
            currentTime.text = MediaPlayerController.mToS(mp.currentPosition)
            handler.postDelayed(runnable, 100)
        }
        handler.postDelayed(runnable, 100)
    }

    private fun createNotificationChannel() {
        val channelId = getString(R.string.channel_id)
        val name = "test"
        val description = "description"
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.description = description
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun notification() {
        val channelId = getString(R.string.channel_id)
        val mBuilder = NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentTitle("YoutubePlayer")
                .setContentText("progress complete!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(1,mBuilder.build())
        }
    }
}