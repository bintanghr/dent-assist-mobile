package com.hibahuns.dentassist.ui.clinics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hibahuns.dentassist.R
import com.hibahuns.dentassist.databinding.ClinicItemBinding
import com.hibahuns.dentassist.ui.home.RvDataItem

class ClinicAdapter() : ListAdapter<RvDataItem, ClinicAdapter.RvViewHolder>(DIFF_CALLBACK){
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RvDataItem>() {
            override fun areItemsTheSame(oldItem: RvDataItem, newItem: RvDataItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RvDataItem, newItem: RvDataItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class RvViewHolder(private val binding: ClinicItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: RvDataItem) {
            binding.apply {
                itemTitle.text = data.title
//                    itemDescription.text = data.description
                Glide.with(itemView.context)
                    .load(data.imageUrl)
                    .error(R.drawable.image_preview)
                    .into(itemImg)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RvViewHolder {
        val view = ClinicItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return RvViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RvViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

}