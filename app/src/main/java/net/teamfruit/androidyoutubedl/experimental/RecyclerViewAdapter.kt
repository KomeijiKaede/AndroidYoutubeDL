package net.teamfruit.androidyoutubedl.experimental

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.teamfruit.androidyoutubedl.R
import net.teamfruit.androidyoutubedl.experimental.database.DBEntity

class RecyclerViewAdapter(private var viewList: List<DBEntity>, private var listener: OnItemClickListener): RecyclerView.Adapter<RecyclerViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(entity: DBEntity)
        fun onItemLongClick(entity: DBEntity)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentList: DBEntity = viewList[position]
        holder.title.text = currentList.title
        holder.bind(currentList, listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(
                LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount() = viewList.size

    fun addList(viewList: List<DBEntity>) {
        this.viewList = viewList
        notifyDataSetChanged()
    }
}


class RecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var title = itemView.findViewById<TextView>(R.id.itemTextView)!!

    fun bind(entity: DBEntity, listener: RecyclerViewAdapter.OnItemClickListener) {
        itemView.setOnClickListener {
            listener.onItemClick(entity)
            return@setOnClickListener
        }
        itemView.setOnLongClickListener {
            listener.onItemLongClick(entity)
            return@setOnLongClickListener false
        }
    }
}