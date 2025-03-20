package com.hibahuns.dentassist.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hibahuns.dentassist.data.api.response.DataItem
import com.bumptech.glide.Glide
import com.hibahuns.dentassist.R
import com.hibahuns.dentassist.databinding.RvItemBinding

class RvAdapter(private val datas: MutableList<RvDataItem>) :
    RecyclerView.Adapter<RvAdapter.RvViewHolder>() {

    inner class RvViewHolder(private val binding: RvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: RvDataItem) {
            binding.apply {
                itemTitle.text = data.title
                itemDescription.text = data.description
                Glide.with(itemView.context)
                    .load(data.imageUrl)
                    .error(R.drawable.image_preview)
                    .into(itemImg)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RvViewHolder {
        val view = RvItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return RvViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RvViewHolder, position: Int) {
        datas[position].let {viewHolder.bind(it)}
    }

    override fun getItemCount() = datas.size

}