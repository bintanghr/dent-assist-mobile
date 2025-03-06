package com.hibahuns.dentassist.ui.notifications

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hibahuns.dentassist.R
import com.hibahuns.dentassist.data.api.response.DataItemHistory
import com.bumptech.glide.Glide
import com.hibahuns.dentassist.databinding.RvItemHistoryBinding
import java.text.DecimalFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
class HistoryAdapter : ListAdapter<DataItemHistory, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemHistory>() {
            override fun areItemsTheSame(oldItem: DataItemHistory, newItem: DataItemHistory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataItemHistory, newItem: DataItemHistory): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(private val binding: RvItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data: DataItemHistory) {
            val accuracy = "Akurasi ${DecimalFormat("#.##").format(data.confidenceScore)}%"
            binding.timestamp.text = formatTimestamp(data.createdAt)
            binding.itemTitle.text = data.label
            binding.itemDescription.text= accuracy
            Glide.with(itemView.context)
                .load(data.imageUrl)
                .placeholder(R.drawable.image_preview)
                .error(R.drawable.image_preview)
                .into(binding.itemImg)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun formatTimestamp(ts: String): String {
            val instant = Instant.parse(ts)
            val localDateTime = instant.atZone(ZoneId.of("Asia/Jakarta"))

            val formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", Locale.ENGLISH)
            val formattedDate = localDateTime.format(formatter)

            return formattedDate.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }
}
