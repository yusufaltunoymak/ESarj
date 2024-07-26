package com.altunoymak.esarj

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.altunoymak.esarj.databinding.ActivityMainBinding
import com.altunoymak.esarj.presentation.viewmodel.ChargingViewModel
import com.altunoymak.esarj.presentation.viewmodel.ConnectivityViewModel
import com.altunoymak.esarj.util.CustomAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val connectivityViewModel : ConnectivityViewModel by viewModels()
    private val viewModel: ChargingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.favoriteStationFragment2,
                R.id.mapsFragment,
                R.id.stationDetailBottomSheet -> binding.bottomNavigationView.isVisible = true
                R.id.searchFragment,
                R.id.splashScreenFragment -> binding.bottomNavigationView.isGone = true
            }
        }
        binding.bottomNavigationView.background = null

        connectivityViewModel.isConnected.observe(this) { isConnected ->
            if (isConnected) {
                viewModel.getChargingStations()
            } else {
                CustomAlertDialogBuilder.createDialog(
                    this,
                    title = getString(R.string.error_text),
                    message = getString(R.string.internet_connection_text),
                    positiveButtonText = getString(R.string.ok_text),
                    positiveButtonClickListener = {}
                )
            }
        }
    }
}