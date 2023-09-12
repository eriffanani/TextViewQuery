package com.erif.textviewquery.ui.adapter.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.erif.library.TextViewQuery
import com.erif.textviewquery.BR
import com.erif.textviewquery.R
import com.erif.textviewquery.databinding.ItemMainRecyclerBinding
import com.erif.textviewquery.model.ModelItemMain
import com.erif.textviewquery.usecases.MainListItemListener
import com.google.android.material.imageview.ShapeableImageView

class AdapterMain constructor(
    private val listener: MainListItemListener? = null
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<ModelItemMain> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Holder(
            ItemMainRecyclerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder)
            holder.bind(list[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: MutableList<ModelItemMain>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return list[position].id.toLong()
    }

    fun getList(): MutableList<ModelItemMain> = list

    inner class Holder constructor(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val img: ShapeableImageView = binding.root.findViewById(R.id.item_main_imgProfile)
        private val txtName: TextViewQuery = binding.root.findViewById(R.id.item_main_txtName)
        fun bind(item: ModelItemMain, position: Int) {
            binding.setVariable(BR.item, item)
            binding.setVariable(BR.position, position)
            img.setImageResource(item.image)
            txtName.query = item.query
            listener?.let { onClick ->
                itemView.setOnClickListener {
                    onClick.onClickItem(item)
                }
            }
        }
    }

}