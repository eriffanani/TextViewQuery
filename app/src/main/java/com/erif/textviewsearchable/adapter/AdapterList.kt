package com.erif.textviewsearchable.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.erif.library.TextViewSearchable
import com.erif.textviewsearchable.R

class AdapterList: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<ModelItemSearch> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_search, parent, false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder)
            holder.bind(list[position])
    }

    private class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val parent: RelativeLayout = itemView.findViewById(R.id.item_parent)
        private val txtSearch: TextViewSearchable = itemView.findViewById(R.id.item_search_txtSearch)

        fun bind(item: ModelItemSearch) {
            txtSearch.text = item.value
            txtSearch.query = item.query
            parent.setOnClickListener {

            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: MutableList<ModelItemSearch>) {
        this.list = list
        notifyDataSetChanged()
    }

}