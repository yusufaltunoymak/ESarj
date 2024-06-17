package com.altunoymak.esarj.presentation.ui.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.altunoymak.esarj.data.model.nearestchargingstation.NearestChargingStation
import com.altunoymak.esarj.databinding.NearestRecyclerItemBinding

class NearestStationAdapter(private val onItemClicked : (NearestChargingStation) -> Unit) : ListAdapter<NearestChargingStation, NearestStationAdapter.ViewHolder>(
    NearestStationDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NearestRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val station = getItem(position)
        holder.bind(station)
    }

    inner class ViewHolder(private val binding: NearestRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(station: NearestChargingStation) {
            binding.titleTextView.text = station.title
            binding.titleTextView.text = station.title
            binding.addressTextView.text = station.address
            binding.providerTextView.text = station.provider

            val acPlugs = mutableListOf<com.altunoymak.esarj.data.model.nearestchargingstation.Plug>()
            val dcPlugs = mutableListOf<com.altunoymak.esarj.data.model.nearestchargingstation.Plug>()

            binding.directionsButton.setOnClickListener {
                onItemClicked(station)
            }

            station.plugs?.forEach { plug ->
                when (plug?.type) {
                    "AC" -> acPlugs.add(plug)
                    "DC" -> dcPlugs.add(plug)
                }
            }

            binding.acTextView.text = if (acPlugs.isNotEmpty()) {
                "AC: ${acPlugs.size} adet / ${acPlugs[0].power}"
            } else {
                "AC: Mevcut değil"
            }

            binding.dcTextView.text = if (dcPlugs.isNotEmpty()) {
                "DC: ${dcPlugs.size} adet / ${dcPlugs[0].power}"
            } else {
                "DC: Mevcut değil"
            }
            binding.distanceTextView.text = "%.2f km".format(station.distance / 1000)
            binding.numberTextView.text = (position + 1).toString()


        }
    }

    class NearestStationDiffCallback : DiffUtil.ItemCallback<NearestChargingStation>() {
        override fun areItemsTheSame(oldItem: NearestChargingStation, newItem: NearestChargingStation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NearestChargingStation, newItem: NearestChargingStation): Boolean {
            return oldItem == newItem
        }
    }
}