package com.altunoymak.esarj.presentation.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.altunoymak.esarj.presentation.viewmodel.ChargingViewModel
import com.altunoymak.esarj.databinding.FragmentSplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {
    private val viewModel: ChargingViewModel by activityViewModels()
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getChargingStations()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewState.collect { viewState ->
                viewState.chargingList?.let { chargingList ->
                    if (chargingList.isNotEmpty()) {
                        navigateToMapsFragment()
                    }
                }
                viewState.errorMessage?.let { errorMessage ->
                    showAlertDialog(errorMessage)
                }
            }
        }
    }

    private fun navigateToMapsFragment() {
        val action = SplashScreenFragmentDirections.actionSplashScreenFragmentToMapsFragment()
        findNavController().navigate(action)
    }

    private fun showAlertDialog(errorMessage : String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(errorMessage)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}