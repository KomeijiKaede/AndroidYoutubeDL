package net.teamfruit.androidyoutubedl.experimental

import android.arch.lifecycle.ViewModelProviders
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.android.synthetic.main.fragment_input.*
import net.teamfruit.androidyoutubedl.R
import net.teamfruit.androidyoutubedl.experimental.database.Base
import net.teamfruit.androidyoutubedl.utils.*
import java.net.URLDecoder

class ExtractFragment: Fragment() {
    private lateinit var appContext: Context
    private var viewModel: RecyclerViewModel? = null
    private var db: Base? = null

    companion object {fun newInstance(): ExtractFragment {return ExtractFragment() }}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
        viewModel = ViewModelProviders.of(this).get(RecyclerViewModel::class.java)
        db = Base.getDataBase(appContext)
    }

    override fun onResume() {
        super.onResume()

        val gsonInstance = Gson()
        val cManager: ClipboardManager = appContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val youtubeUrl = inputURLBox as EditText

        parse.setOnClickListener {
            statusViewer.text = "ParsingURI"

            val videoID : String

            try {
                videoID = getYoutubeUriByVid(youtubeUrl.text.toString())
            } catch (e: IllegalArgumentException) {
                return@setOnClickListener
            }

            statusViewer.text = "Get video info by youtube.com"
            "https://www.youtube.com/get_video_info?video_id=$videoID".httpGet().response { _, response, result ->
                when(result) {
                    is Result.Success -> {
                        statusViewer.text = "Parsing JSON"
                        val parsedJson : PlayerResponse

                        try {
                            parsedJson = gsonInstance.fromJson(
                                    URLDecoder.decode(
                                            getStringValueByUrlParameters(
                                                    response.toString(),
                                                    "player_response"
                                            ),
                                            "UTF-8"
                                    ),
                                    PlayerResponse::class.java
                            )
                        } catch (e: JsonSyntaxException) {
                            // Google returns not valid json if api limitv
                            statusViewer.text = "Detected Google API Limit."
                            return@response
                        }

                        // status check (parsedJson.streamingData is nullable if enabled copyright protection)
                        if (parsedJson.playabilityStatus.status == "UNPLAYABLE") {
                            statusViewer.text = "Detected copyright protection."
                            return@response
                        }

                        val format = getBestQualityAudioFormatByAdaptiveFormats(
                                parsedJson.streamingData.adaptiveFormats
                        )

                        val details = parsedJson.videoDetails

                        // check format (if hasn't audio format in adaptiveFormats)
                        if(format === null) {
                            statusViewer.text = "Audio format not in adaptiveFormats"
                            return@response
                        }
                        MediaPlayerController.prepareStart(Uri.parse(format.url).toString())
                        viewModel!!.addList(Base.create(details.title, videoID))
                    }

                    is Result.Failure -> {
                        statusViewer.text = "Failed to Get video info"
                        return@response
                    }
                }
            }
        }

        pasteButton.setOnClickListener {
            val clipboardText : CharSequence? = cManager.primaryClip?.getItemAt(0)?.text

            if (clipboardText !== null) {
                youtubeUrl.setText(clipboardText)
            }
        }
    }
}