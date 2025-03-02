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

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    inner class RvViewHolder(private val binding: RvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: RvDataItem) {
            binding.apply {
                itemTitle.text = data.title
                itemDescription.text = data.description
//                Log.d("ClinikAdapter", "Memuat gambar dari URL: ${clinic.photo}")
                Glide.with(itemView.context)
                    .load(data.imageUrl)
                    .error(R.drawable.image_preview)
                    .into(itemImg)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RvViewHolder {
        // Create a new view, which defines the UI of the list item
//        val view = LayoutInflater.from(viewGroup.context)
//            .inflate(R.layout.rv_item, viewGroup, false)
        val view = RvItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return RvViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: RvViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        datas[position].let {viewHolder.bind(it)}
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = datas.size

}