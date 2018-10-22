package net.teamfruit.androidyoutubedl.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_audioplayer.*
import net.teamfruit.androidyoutubedl.utils.MediaPlayerController
import net.teamfruit.androidyoutubedl.R

class AudioPlayerFragment : Fragment() {
    private val mp = MediaPlayerController.mp
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private lateinit var appContext:Context

    companion object {fun newInstance():AudioPlayerFragment{return AudioPlayerFragment()}}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_audioplayer, container, false)
    }

    override fun onResume() {
        super.onResume()
        playButton.setOnClickListener {
            when(mp.isPlaying) {
                true -> {
                    mp.pause()
                    playButton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp)
                }
                false -> {
                    mp.start()
                    initSeekBar()
                    playButton.setBackgroundResource(R.drawable.ic_pause_black_24dp)
                }
            }
        }

        repeatButton.setOnClickListener {
            when (mp.isLooping) {
                false -> {
                    mp.isLooping = true
                    Toast.makeText(appContext, "looping", Toast.LENGTH_SHORT).show()
                }
                true -> {
                    mp.isLooping = false
                    Toast.makeText(appContext, "not looping", Toast.LENGTH_SHORT).show()
                }
            }
        }

        audioSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mp.seekTo(progress)
                    currentTimeView.text = MediaPlayerController.mToS(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun initSeekBar() {
        audioSeekBar.max = mp.duration
        runnable = Runnable {
            audioSeekBar.progress = mp.currentPosition
            currentTimeView.text = MediaPlayerController.mToS(mp.currentPosition)
            handler.postDelayed(runnable, 100)
        }
        handler.postDelayed(runnable, 100)
    }
}