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
import kotlinx.android.synthetic.main.fragment_audioplayer.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {
    private var audioUrl: String? = null
    private val mp = MediaPlayerController.mp
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
                var title:String?
                audioUrl = null
                textView2.text = ""
                job = async(UI) {
                    title = getVideoTitle(youtubeUrl)
                    textView3.text = title
                    if (audioUrl != null) launch { mp.reset() }.join()
                    audioUrl = getUrlTask(youtubeUrl)
                    notification(title!!)
                    textView2.text = audioUrl
                    mp.setDataSource(audioUrl)
                    mp.prepare()
                    mp.start()
                }
            }
        }
    }

    private suspend fun getUrlTask(inputURL: String): String {
        return if (getVideoInfo(inputURL) == "audioURL not found") "audioURL not found" else statusCheck(inputURL)
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

    private fun notification(msg:String) {
        val channelId = getString(R.string.channel_id)
        val mBuilder = NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentTitle("YoutubePlayer")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(1,mBuilder.build())
        }
    }
}