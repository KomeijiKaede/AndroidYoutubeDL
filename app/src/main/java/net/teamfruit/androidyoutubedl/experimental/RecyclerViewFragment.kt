package net.teamfruit.androidyoutubedl.experimental

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.android.synthetic.main.fragment_listview.*
import net.teamfruit.androidyoutubedl.R
import net.teamfruit.androidyoutubedl.experimental.database.Base
import net.teamfruit.androidyoutubedl.experimental.database.DBEntity
import net.teamfruit.androidyoutubedl.utils.MediaPlayerController
import net.teamfruit.androidyoutubedl.utils.PlayerResponse
import net.teamfruit.androidyoutubedl.utils.getBestQualityAudioFormatByAdaptiveFormats
import net.teamfruit.androidyoutubedl.utils.getStringValueByUrlParameters
import java.net.URLDecoder

class RecyclerViewFragment: Fragment(), RecyclerViewAdapter.OnItemClickListener {
    private lateinit var appContext: Context
    private var viewModel: RecyclerViewModel? = null
    private var db: Base? = null
    private var adapter: RecyclerViewAdapter? = null

    companion object {fun newInstance(): RecyclerViewFragment {return RecyclerViewFragment()}}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_listview, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
        viewModel = ViewModelProviders.of(this).get(RecyclerViewModel::class.java)
        db = Base.getDataBase(appContext)
    }

    override fun onResume() {
        super.onResume()
        adapter = RecyclerViewAdapter(arrayListOf(), this)
        mainRecyclerView.adapter = adapter
        mainRecyclerView.layoutManager = LinearLayoutManager(appContext)
        viewModel!!.getList().observe(this, Observer { DBEntity ->
            adapter!!.addList(DBEntity!!)
        })
    }

    override fun onItemClick(entity: DBEntity) {
        val gsonInstance = Gson()
        "https://www.youtube.com/get_video_info?video_id=${entity.videoID}".httpGet().response { _, response, result ->
            when(result) {
                is Result.Success -> {
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
                        return@response
                    }

                    if (parsedJson.playabilityStatus.status == "UNPLAYABLE") return@response

                    val format = getBestQualityAudioFormatByAdaptiveFormats(
                            parsedJson.streamingData.adaptiveFormats
                    )

                    if(format === null) return@response

                    MediaPlayerController.prepareStart(Uri.parse(format.url).toString())
                }

                is Result.Failure -> {
                    return@response
                }
            }
        }
    }

    override fun onItemLongClick(entity: DBEntity) {
        db!!.dbdao().delete(entity)
        adapter!!.notifyDataSetChanged()
    }
}