package com.erif.textviewquery.ui.adapter.serach

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erif.textviewquery.BR
import com.erif.textviewquery.databinding.ItemSearchBinding
import com.erif.textviewquery.model.ModelItemSearch

class AdapterSearch: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<ModelItemSearch> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Holder(
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder)
            holder.bind(list[position])
    }

    private class Holder(
        private val binding: ItemSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val parent = binding.itemParent
        private val txtSearch = binding.itemSearchTxtSearch

        fun bind(item: ModelItemSearch) {
            //txtSearch.text = item.value
            txtSearch.query = item.query
            binding.setVariable(BR.item, item)
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