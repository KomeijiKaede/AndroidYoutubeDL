package net.teamfruit.androidyoutubedl.ui

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.android.synthetic.main.fragment_input.*
import kotlinx.android.synthetic.main.fragment_listview.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import net.teamfruit.androidyoutubedl.R
import net.teamfruit.androidyoutubedl.db.DBOpenHelper
import net.teamfruit.androidyoutubedl.db.ListData
import net.teamfruit.androidyoutubedl.db.ListDataParser
import net.teamfruit.androidyoutubedl.utils.*
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import java.net.URLDecoder

class MusicListFragment: Fragment(), RecyclerViewHolder.ItemClickLister {
    private lateinit var appContext: Context

    companion object {fun newInstance(): MusicListFragment {return MusicListFragment()}}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_listview, container, false)
    }

    override fun onResume() {
        super.onResume()
        val dataList = mutableListOf<String>()
        dataList.add("Stay For A While")
        dataList.add("NULCTRL")
        dataList.add("Night Sky")
        dataList.add("DayDream")
        dataList.add("In My Mind")
        mainRecyclerView.adapter = RecyclerAdapter(appContext, this, dataList)
        mainRecyclerView.layoutManager = LinearLayoutManager(appContext, LinearLayoutManager.VERTICAL, false)
    }

    override fun onItemClick(view: View, position: Int) {
        val list = mutableListOf<String>()
        list.add("https://www.youtube.com/watch?v=pQcchiPuMIQ")//Stay For A While
        list.add("https://www.youtube.com/watch?v=D-MF_Vbgs94")//NULCTRL
        list.add("https://www.youtube.com/watch?v=d9XXocCbng4")//Night Sky
        list.add("https://www.youtube.com/watch?v=QB6Zkv0Oqoo")//DayDream
        list.add("https://www.youtube.com/watch?v=mZ_BSasRK84")//In My Mind

        val videoID = getYoutubeUriByVid(list[position])
        val audioURL = getAudioUrl(videoID)
        MediaPlayerController.prepareStart(audioURL)
    }

    override fun onLongItemClick(view: View, position: Int) {

    }
}