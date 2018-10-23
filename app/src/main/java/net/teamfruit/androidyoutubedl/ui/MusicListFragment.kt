package net.teamfruit.androidyoutubedl.ui

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.fragment_listview.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import net.teamfruit.androidyoutubedl.R
import net.teamfruit.androidyoutubedl.db.DBOpenHelper
import net.teamfruit.androidyoutubedl.db.ListData
import net.teamfruit.androidyoutubedl.db.ListDataParser
import net.teamfruit.androidyoutubedl.utils.ExtractURL
import net.teamfruit.androidyoutubedl.utils.MediaPlayerController
import org.jetbrains.anko.db.select

class MusicListFragment: Fragment(), RecyclerViewHolder.ItemClickLister {
    private lateinit var appContext: Context
    private lateinit var helper: SQLiteDatabase
    private lateinit var dataList: List<ListData>
    private val mp = MediaPlayerController.mp
    private var job: Deferred<Unit>? = null
    private val extUrl = ExtractURL.newInstance()

    companion object {fun newInstance(): MusicListFragment {return MusicListFragment()}}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
        helper = DBOpenHelper.newInstance(appContext).writableDatabase
        dataList = helper.select(DBOpenHelper.tableName).parseList(ListDataParser())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_listview, container, false)
    }

    override fun onResume() {
        super.onResume()
        mainRecyclerView.adapter = RecyclerAdapter(appContext, this, dataList)
        mainRecyclerView.layoutManager = LinearLayoutManager(appContext, LinearLayoutManager.VERTICAL, false)
    }

    override fun onItemClick(view: View, position: Int) {
        mp.reset()
        mp.setDataSource(dataList[position].url)
        mp.prepare()
        mp.start()
        Toast.makeText(appContext, dataList[position].title, Toast.LENGTH_SHORT).show()
    }

    override fun onLongItemClick(view: View, position: Int) {
        val data = dataList[position]
        var audioURL: String? = null
        mp.reset()
        job = async {
            audioURL = checkURL(data.url!!, data.originURL!!)
            mp.setDataSource(audioURL)
            mp.prepare()
            mp.start()
        }
    }

    private suspend fun checkURL(audioURL: String, inputURL: String):String {
        return async {
            return@async if(audioURL.httpGet().response().second.statusCode == 403) {
                getUrlTask(inputURL)
            } else { audioURL }
        }.await()
    }

    private suspend fun getUrlTask(inputURL: String): String {
        return if (extUrl.getVideoInfo(inputURL) == "audioURL not found") "audioURL not found" else extUrl.statusCheck(inputURL)
    }
}