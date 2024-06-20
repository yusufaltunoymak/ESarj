package com.altunoymak.esarj.presentation.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.altunoymak.esarj.R
import com.altunoymak.esarj.databinding.FragmentFavoriteStationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class FavoriteStationFragment : Fragment() {
    private var _binding: FragmentFavoriteStationBinding? = null
    private val binding get() = _binding!!
    private val favoriteViewModel: FavoriteViewModel by activityViewModels()
    private lateinit var favoriteStationAdapter: FavoriteStationAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteStationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteStationAdapter = FavoriteStationAdapter({ favoriteStation ->
            favoriteViewModel.selectStation(favoriteStation)
            val action = FavoriteStationFragmentDirections.actionFavoriteStationFragment2ToMapsFragment()
            findNavController().navigate(action)
        }, { favoriteStation ->
            favoriteViewModel.removeFavoriteStation(favoriteStation)
        })
        binding.favoriteRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.favoriteRv.adapter = favoriteStationAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            favoriteViewModel.favoriteStations.collect {
                favoriteStationAdapter.submitList(it)
            }
        }
        binding.favoriteToolbar.title = getString(R.string.favourite_station)
        binding.favoriteToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}