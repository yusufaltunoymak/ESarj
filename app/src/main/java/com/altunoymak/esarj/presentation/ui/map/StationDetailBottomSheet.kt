package com.altunoymak.esarj.presentation.ui.map

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.altunoymak.esarj.data.model.chargingstationdetail.DetailStation
import com.altunoymak.esarj.data.model.chargingstationdetail.Plug
import com.altunoymak.esarj.databinding.FragmentStationDetailBottomSheetBinding
import com.altunoymak.esarj.presentation.ui.favorite.FavoriteViewModel
import com.altunoymak.esarj.util.toFavoriteStationEntity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StationDetailBottomSheet(private val detail: DetailStation) : BottomSheetDialogFragment() {

    private var _binding: FragmentStationDetailBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val favoriteViewModel : FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStationDetailBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleTextView.text = detail.title
        binding.addressTextView.text = detail.address
        binding.providerTextView.text = detail.provider


        Log.d("StationDetailBottomSheet", "pointOfInterests: ${detail.pointOfInterests}")

        detail.pointOfInterests?.forEach { pointOfInterest ->
            Log.d("StationDetailBottomSheet", "Creating chip for: $pointOfInterest")
            val chip = Chip(context)
            chip.text = pointOfInterest.toString()
            binding.chipGroup.addView(chip)
        }
        val acPlugs = mutableListOf<Plug>()
        val dcPlugs = mutableListOf<Plug>()

        detail.plugs?.forEach { plug ->
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
        binding.directionsButton.setOnClickListener {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=${detail.location?.lat},${detail.location?.lon}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
        detail.id?.let {
            val station = detail.toFavoriteStationEntity()
            val isFavoriteStationLiveData = favoriteViewModel.isFavoriteStation(it)
            isFavoriteStationLiveData.observe(viewLifecycleOwner) { isFavorite ->
                if (isFavorite) {
                    binding.favoriteIv.isSelected = true
                } else {
                    binding.favoriteIv.isSelected = false
                }
            }

            binding.favoriteIv.setOnClickListener {
                if (binding.favoriteIv.isSelected) {
                    favoriteViewModel.removeFavoriteStation(station)
                } else {
                    favoriteViewModel.addFavoriteStation(station)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}