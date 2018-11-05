package net.teamfruit.androidyoutubedl.ui

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update

class MusicListFragment: Fragment(), RecyclerViewHolder.ItemClickLister {
    private lateinit var appContext: Context
    private lateinit var helper: SQLiteDatabase
    private lateinit var dataList: List<ListData>
    private val mp = MediaPlayerController
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
        job = async { mp.prepareStart(checkURL(dataList[position].url!!, dataList[position].originURL!!)) }
    }

    override fun onLongItemClick(view: View, position: Int) {
        helper.delete(DBOpenHelper.tableName, "title = {key}", "key" to dataList[position])
    }

    private suspend fun checkURL(url: String, originURL: String): String {
        return async {
            if(url.httpGet().response().second.statusCode == 403) {
                val newUrl = getUrlTask(originURL)
                helper.update(DBOpenHelper.tableName, "url" to newUrl)
                        .`whereSimple`("originURL = ?", originURL)
                        .exec()
                return@async newUrl
            } else return@async url
        }.await()
    }

    private suspend fun getUrlTask(inputURL: String): String {
        return if (extUrl.getVideoInfo(inputURL) == "audioURL not found") "audioURL not found" else extUrl.statusCheck(inputURL)
    }
}