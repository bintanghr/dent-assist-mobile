package com.hibahuns.dentassist.ui.notifications

import android.os.Build
import android.text.Html
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
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Suppress("DEPRECATION")
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
        fun bind(data: DataItemHistory) {
            binding.timestamp.text = formatTimestamp(data.createdAt)
            binding.itemTitle.text = data.label
            binding.itemDescription.text= Html.fromHtml(
                "Akurasi ${String.format("%.2f", data.confidenceScore)}%"
            )
            Glide.with(itemView.context)
                .load(data.imageUrl)
                .placeholder(R.drawable.image_preview)
                .error(R.drawable.image_preview)
                .into(binding.itemImg)



        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun formatTimestamp(ts: String): String {
            val input = "2025-03-02T15:50:03.090Z"

            // Parsing string ke Instant (UTC)
            val instant = Instant.parse(input)

            // Konversi ke zona waktu lokal (WIB, UTC+7 misalnya)
            val zoneId = ZoneId.of("Asia/Jakarta")
            val localDateTime = instant.atZone(zoneId)

            // Format tanggal sesuai keinginan
            val formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", Locale.ENGLISH)
            val formattedDate = localDateTime.format(formatter)

            return formattedDate.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }
}
