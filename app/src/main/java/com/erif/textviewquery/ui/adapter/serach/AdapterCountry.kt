package com.erif.textviewquery.ui.adapter.serach

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erif.textviewquery.databinding.ItemCountryBinding

class AdapterCountry: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(ItemCountryBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (holder is Holder)
            holder.bind(item)
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: MutableList<String>) {
        this.list = list
        notifyDataSetChanged()
    }

    class Holder(
        binding: ItemCountryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val txt = binding.txtCountry

        fun bind(item: String) {
            txt.text = item
        }
    }

}