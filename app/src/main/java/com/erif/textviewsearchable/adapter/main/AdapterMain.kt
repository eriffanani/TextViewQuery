package com.erif.textviewsearchable.adapter.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erif.textviewsearchable.R
import com.google.android.material.imageview.ShapeableImageView

class AdapterMain: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<ModelItemMain> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_main_recycler, parent, false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder)
            holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: MutableList<ModelItemMain>) {
        this.list = list
        notifyDataSetChanged()
    }

    private class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val parentView: LinearLayout = itemView.findViewById(R.id.item_main_parent)
        private val img: ShapeableImageView = itemView.findViewById(R.id.item_main_imgProfile)
        private val txtName: TextView = itemView.findViewById(R.id.item_main_txtName)
        private val txtSubtitle: TextView = itemView.findViewById(R.id.item_main_txtSubtitle)
        fun bind(item: ModelItemMain) {
            img.setImageResource(item.image)
            txtName.text = item.name
            txtSubtitle.text = item.subtitle
            parentView.setOnClickListener {

            }
        }
    }

}