package com.erif.textviewquery.ui.adapter.serach

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.erif.library.TextViewQuery
import com.erif.textviewquery.BR
import com.erif.textviewquery.R
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

    private class Holder constructor(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val parent: RelativeLayout = binding.root.findViewById(R.id.item_parent)
        private val txtSearch: TextViewQuery = binding.root.findViewById(R.id.item_search_txtSearch)

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