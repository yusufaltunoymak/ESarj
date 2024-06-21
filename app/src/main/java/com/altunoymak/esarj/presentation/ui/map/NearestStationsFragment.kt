package com.altunoymak.esarj.presentation.ui.map

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.altunoymak.esarj.R
import com.altunoymak.esarj.databinding.FragmentNearestStationBinding
import com.altunoymak.esarj.presentation.viewmodel.NearestStationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NearestStationsFragment : Fragment() {
    private val viewModel: NearestStationViewModel by viewModels()
    private lateinit var adapter: NearestStationAdapter
    private var _binding: FragmentNearestStationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNearestStationBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNearestStationBinding.bind(view)
        val args : NearestStationsFragmentArgs by navArgs()
        val lat = args.latitude
        val lon = args.longitude
        initAdapter()
        binding.nearestToolbar.title = getString(R.string.en_yakin_sarj_istasyonlari)
        binding.nearestToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.fetchNearestChargingStations(lat.toDouble(), lon.toDouble())


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewState.collect { viewState ->
                if (viewState.nearestList.isNullOrEmpty()) {
                    binding.errorMessageTv.visibility = View.VISIBLE
                    binding.errorMessageTv.text = getString(R.string.not_found_charging_station)
                }
                else {
                    adapter.submitList(viewState.nearestList)
                }
                viewState.isLoading?.let {
                    if (it) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                }
                viewState.errorMessage?.let {
                    binding.errorMessageTv.visibility = View.VISIBLE
                    binding.errorMessageTv.text = it
                }
            }
        }
    }

    private fun initAdapter() {
        adapter = NearestStationAdapter {
            val gmmIntentUri = Uri.parse("google.navigation:q=${it.location?.lat},${it.location?.lon}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}