package com.altunoymak.esarj.presentation.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.altunoymak.esarj.R
import com.altunoymak.esarj.data.model.chargingstationdetail.DetailStation
import com.altunoymak.esarj.databinding.FragmentMapsBinding
import com.altunoymak.esarj.presentation.viewmodel.ChargingViewModel
import com.altunoymak.esarj.presentation.viewmodel.SearchViewModel
import com.altunoymak.esarj.util.ClusterItem
import com.altunoymak.esarj.util.CustomClusterRenderer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.clustering.ClusterManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    private lateinit var locationManager: LocationManager
    private var trackBoolean: Boolean? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val viewModel: ChargingViewModel by activityViewModels()
    private val searchViewModel : SearchViewModel by activityViewModels()

    private var shouldUpdateLocation = true
    private var shouldNavigate = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        registerLauncher()

        sharedPreferences =
            requireActivity().getSharedPreferences("com.altunoymak.esarj", Context.MODE_PRIVATE)
        trackBoolean = false
        sharedPreferences.edit().putBoolean("trackBoolean", false).apply()


        return binding.root
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val turkey = LatLng(39.9334, 32.8597)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(turkey, 5.0f))

        val clusterManager = ClusterManager<ClusterItem>(requireContext(), mMap)
        val clusterRenderer = CustomClusterRenderer(requireContext(), mMap, clusterManager)
        clusterManager.renderer = clusterRenderer

        mMap.setOnCameraIdleListener(clusterManager)
        mMap.setOnMarkerClickListener(clusterManager)
        observeSearchResult()

        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                findNavController().navigate(R.id.searchFragment)
            }
        }


        binding.zoomInButton.setOnClickListener {
            shouldUpdateLocation = true
            shouldNavigate = true
            permissionToLocation()
        }

        binding.zoomOutButton.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }

        binding.currentLocationIv.setOnClickListener {
            shouldUpdateLocation = true
            permissionToLocation()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewState.collect { viewState ->
                viewState.isLoading?.let {
                    if (it) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                }
                viewState.chargingList?.let { chargingStations ->
                    clusterManager.clearItems()
                    chargingStations.forEach { station ->
                        station?.location?.let {
                            val location = LatLng(it.lat ?: 0.0, it.lon ?: 0.0)
                            val clusterItem = ClusterItem(
                                location,
                                station.provider ?: "",
                                station.provider ?: "",
                                id = station.id!!
                            )
                            clusterManager.addItem(clusterItem)
                        }
                    }
                    clusterManager.cluster()
                }
                viewState.chargingDetail?.let {
                    showStationDetailBottomSheet(it)
                }
            }
        }
        clusterManager.setOnClusterItemClickListener { item ->
            val chargingStationId = item.id
            viewModel.fetchChargingStationDetail(chargingStationId)
            true
        }

        permissionToLocation()
    }

    private fun permissionToLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // İzin isteme
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                Snackbar.make(
                    binding.root,
                    "Konumunuza erişmek için izin vermelisiniz",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("İzin Ver") {
                    permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }.show()
            } else {
                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        } else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // Konum servisleri etkin değil, uyarı göster
                AlertDialog.Builder(requireContext())
                    .setMessage("Konum hizmetleri etkin değil. Lütfen konum hizmetlerini etkinleştirin.")
                    .setPositiveButton("Tamam", null)
                    .show()
            } else {
                // trackBoolean'ı sıfırla
                sharedPreferences.edit().putBoolean("trackBoolean", false).apply()
                if (shouldUpdateLocation) {
                    binding.progressBar.visibility = View.VISIBLE
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0f,
                        locationListener
                    )
                    val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (lastLocation != null) {
                        updateLocationUI(lastLocation)
                    }
                }
            }
        }
    }


    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            updateLocationUI(location)

        }

    }

    private fun updateLocationUI(location: Location) {
        binding.progressBar.visibility = View.GONE
        if (shouldUpdateLocation) {
            val trackBoolean = sharedPreferences.getBoolean("trackBoolean", false)
            if (!trackBoolean) {
                mMap.clear()
                val userLocation = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(userLocation).title("Şuan Buradasın"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                sharedPreferences.edit().putBoolean("trackBoolean", true).apply()
                if (shouldNavigate) {
                    shouldNavigate = false
                    val action = MapsFragmentDirections.actionMapsFragmentToNearestStationsFragment(
                        userLocation.latitude.toFloat(),
                        userLocation.longitude.toFloat()
                    )
                    findNavController().navigate(action)
                }
            }
        }
    }



    private fun registerLauncher() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                //permission granted
                if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (shouldUpdateLocation) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
                        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        if (lastLocation != null) {
                            val lastUserLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
                            mMap.addMarker(MarkerOptions().position(lastUserLocation).title("Şuan Buradasın"))
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 15f))
                        }
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "İzin Gerekli!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showStationDetailBottomSheet(detail: DetailStation) {
        val bottomSheet = StationDetailBottomSheet(detail)
        view?.post {
            if (isAdded) {
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            }
        }
    }

    private fun observeSearchResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.selectedSuggestion.observe(viewLifecycleOwner) { suggestion ->
                shouldUpdateLocation = false
                val location = LatLng(suggestion.chargingStation!!.location!!.lat!!, suggestion.chargingStation.location!!.lon!!)
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15f)
                mMap.moveCamera(cameraUpdate)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.clearChargingDetail()
        }
    }
}
