package com.altunoymak.esarj.presentation.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.altunoymak.esarj.data.local.FavoriteStationEntity
import com.altunoymak.esarj.databinding.FavoriteStationItemBinding

class FavoriteStationAdapter(private val onItemClicked : (FavoriteStationEntity) -> Unit,private val onRemoveClick: (FavoriteStationEntity) -> Unit) : ListAdapter<FavoriteStationEntity, FavoriteStationAdapter.ViewHolder>(FavoriteStationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FavoriteStationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val station = getItem(position)
        holder.bind(station)
    }

    inner class ViewHolder(private val binding: FavoriteStationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(station: FavoriteStationEntity) {
            binding.apply {
                nameTextView.text = station.title
                addressTextView.text = station.address
                removeIv.setOnClickListener {
                    onRemoveClick(station)
                }
            }
            binding.root.setOnClickListener {
                onItemClicked(station)
            }
        }
    }

    class FavoriteStationDiffCallback : DiffUtil.ItemCallback<FavoriteStationEntity>() {
        override fun areItemsTheSame(oldItem: FavoriteStationEntity, newItem: FavoriteStationEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteStationEntity, newItem: FavoriteStationEntity): Boolean {
            return oldItem == newItem
        }
    }
}