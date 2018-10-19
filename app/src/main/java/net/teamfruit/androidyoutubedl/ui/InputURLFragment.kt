package net.teamfruit.androidyoutubedl.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_extractor.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import net.teamfruit.androidyoutubedl.R
import net.teamfruit.androidyoutubedl.db.DBOpenHelper
import net.teamfruit.androidyoutubedl.utils.ExtractURL
import net.teamfruit.androidyoutubedl.utils.MediaPlayerController
import org.jetbrains.anko.db.insert

class InputURLFragment : Fragment(){
    private val mp = MediaPlayerController.mp
    private var job: Deferred<Unit>? = null
    private val extUrl = ExtractURL.newInstance()
    private lateinit var appContext:Context

    companion object { fun newInstance(): InputURLFragment { return InputURLFragment() } }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_extractor, container, false)
    }

    override fun onResume() {
        super.onResume()
        /*inputButton.setOnClickListener {
            val youtubeURL:String
            var audioURL:String? = null

            if(inputButton.text != null) {
                youtubeURL = inputURLBox.text.toString()
                if(audioURL != null){mp.reset()}
                job = async(UI) {
                    audioURL = getUrlTask(youtubeURL)
                    titleView.text = extUrl.getVideoTitle(youtubeURL)
                    mp.setDataSource(audioURL)
                    mp.prepare()
                }
            }
        }*/

        inputButton.setOnClickListener {
            val youtubeURL:String
            var audioURL:String? = null
            var title:String? = null
            val helper = DBOpenHelper.newInstance(appContext)

            if(inputURLBox.text != null) {
                youtubeURL = inputURLBox.text.toString()
                job = async(UI) {
                    audioURL = getUrlTask(youtubeURL)
                    title = extUrl.getVideoTitle(youtubeURL)
                    titleView.text = title
                    helper.use {
                        insert(DBOpenHelper.tableName, *arrayOf("title" to title, "url" to audioURL))
                    }
                    mp.setDataSource(audioURL)
                    mp.prepare()
                }
            }
        }
    }

    private suspend fun getUrlTask(inputURL: String): String {
        return if (extUrl.getVideoInfo(inputURL) == "audioURL not found") "audioURL not found" else extUrl.statusCheck(inputURL)
    }
}