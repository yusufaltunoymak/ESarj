package com.altunoymak.esarj.presentation.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.altunoymak.esarj.R
import com.altunoymak.esarj.presentation.viewmodel.ChargingViewModel
import com.altunoymak.esarj.databinding.FragmentSplashScreenBinding
import com.altunoymak.esarj.presentation.viewmodel.ConnectivityViewModel
import com.altunoymak.esarj.util.CustomAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {
    private val viewModel: ChargingViewModel by activityViewModels()
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    private val connectivityViewModel: ConnectivityViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectivityViewModel.isConnected.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                viewModel.getChargingStations()
            } else {
                CustomAlertDialogBuilder.createDialog(
                    requireContext(),
                    title = getString(R.string.error_text),
                    message = getString(R.string.internet_connection_text),
                    positiveButtonText = getString(R.string.ok_text),
                    positiveButtonClickListener = {}
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewState.collect { viewState ->
                viewState.chargingList?.let { chargingList ->
                    if (chargingList.isNotEmpty()) {
                        navigateToMapsFragment()
                    }
                }
                viewState.errorMessage?.let { errorMessage ->
                    CustomAlertDialogBuilder.createDialog(
                        requireContext(),
                        title = getString(R.string.warning_text),
                        message = errorMessage,
                        positiveButtonText = getString(R.string.ok_text),
                        positiveButtonClickListener = {}
                    )
                }
            }
        }
    }
    private fun navigateToMapsFragment() {
        val action = SplashScreenFragmentDirections.actionSplashScreenFragmentToMapsFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}