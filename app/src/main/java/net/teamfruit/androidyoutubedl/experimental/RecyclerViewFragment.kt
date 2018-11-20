package net.teamfruit.androidyoutubedl.experimental

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_listview.*
import net.teamfruit.androidyoutubedl.R
import net.teamfruit.androidyoutubedl.experimental.database.Base
import net.teamfruit.androidyoutubedl.experimental.database.DBEntity

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

    }

    override fun onItemLongClick(entity: DBEntity) {
        db!!.dbdao().delete(entity)
        adapter!!.notifyDataSetChanged()
    }
}