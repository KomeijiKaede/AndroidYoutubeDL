package net.teamfruit.androidyoutubedl.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import net.teamfruit.androidyoutubedl.R

class RecyclerViewHolder(view: View): RecyclerView.ViewHolder(view) {
    interface ItemClickLister {
        fun onItemClick(view: View,position: Int)
        fun onLongItemClick(view: View,position: Int)
    }

    val itemTextView: TextView = view.findViewById(R.id.itemTextView)
    val itemImageView: ImageView = view.findViewById(R.id.itemImageView)
}