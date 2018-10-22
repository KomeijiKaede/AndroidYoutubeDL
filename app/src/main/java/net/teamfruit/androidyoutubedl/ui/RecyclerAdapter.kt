package net.teamfruit.androidyoutubedl.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.teamfruit.androidyoutubedl.R
import net.teamfruit.androidyoutubedl.db.ListData

class RecyclerAdapter(private val context: Context, private val itemClickListener: RecyclerViewHolder.ItemClickLister, private val itemList: List<ListData>) : RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mRecyclerView : RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null

    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val text: String? = if(itemList[position].title == null) "null" else itemList[position].title
        holder.let {
            it.itemTextView.text = text
            it.itemImageView.setImageResource(R.mipmap.ic_launcher)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val mView = layoutInflater.inflate(R.layout.list_item, parent, false)

        mView.setOnClickListener { view ->
            mRecyclerView?.let {
                itemClickListener.onItemClick(view, it.getChildAdapterPosition(view))
            }
            return@setOnClickListener
        }

        mView.setOnLongClickListener { view ->
            mRecyclerView?.let {
                itemClickListener.onLongItemClick(view, it.getChildAdapterPosition(view))
            }
            return@setOnLongClickListener true
        }

        return RecyclerViewHolder(mView)
    }

}