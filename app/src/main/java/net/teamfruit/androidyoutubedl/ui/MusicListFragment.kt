package net.teamfruit.androidyoutubedl.ui

import android.content.Context
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import net.teamfruit.androidyoutubedl.R
import net.teamfruit.androidyoutubedl.db.DBOpenHelper
import net.teamfruit.androidyoutubedl.db.ListData
import net.teamfruit.androidyoutubedl.db.ListDataParser
import net.teamfruit.androidyoutubedl.utils.MediaPlayerController
import org.jetbrains.anko.db.select

class MusicListFragment: Fragment(), RecyclerViewHolder.ItemClickLister {
    private val listContents = mutableListOf<ListData>()
    private lateinit var appContext: Context
    private val mp = MediaPlayerController.mp

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
        val helper = DBOpenHelper.newInstance(appContext)
        val dataList = helper.readableDatabase.select(DBOpenHelper.tableName).parseList(ListDataParser())
        listContents.addAll(dataList)
        mainRecyclerView.adapter = RecyclerAdapter(appContext, this, listContents)
        mainRecyclerView.layoutManager = LinearLayoutManager(appContext, LinearLayoutManager.VERTICAL, false)
    }

    override fun onItemClick(view: View, position: Int) {
        val helper = DBOpenHelper.newInstance(appContext)
        val dataList = helper.readableDatabase.select(DBOpenHelper.tableName).parseList(ListDataParser())
        mp.reset()
        mp.setDataSource(dataList[position].url)
        mp.prepare()
        mp.start()
    }
}