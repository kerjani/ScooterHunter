package com.kernacs.scooterhunter.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.clustering.ClusterManager
import com.kernacs.scooterhunter.R
import com.kernacs.scooterhunter.base.BaseFragment
import com.kernacs.scooterhunter.data.entity.ScooterEntity
import com.kernacs.scooterhunter.databinding.FragmentScootersMapBinding
import com.kernacs.scooterhunter.ui.details.ScooterDetailsFragment
import com.kernacs.scooterhunter.util.EmptyDataException
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.floor


@AndroidEntryPoint
class ScootersMapFragment : BaseFragment(), OnMapReadyCallback {

    private var isMapReady: Boolean = false

    private val viewModel: ScootersViewModel by viewModels()
    private lateinit var binding: FragmentScootersMapBinding

    private lateinit var map: GoogleMap

    private lateinit var clusterManager: ClusterManager<ScooterClusterItem>

    override val trackedScreenName: String
        get() = "ScootersMap"

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                enableMyLocation()
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                val snack = Snackbar.make(
                    requireView(),
                    R.string.explain_location_permission,
                    Snackbar.LENGTH_LONG
                )
                snack.show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScootersMapBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.data.observe(this as LifecycleOwner, { data ->
            if (data.isEmpty())
                viewModel.error.value = EmptyDataException()
            else {
                if (isMapReady) {
                    loadMarkers(data)
                }
            }
        })
        viewModel.isLoading.observe(this as LifecycleOwner, {
            binding.progressIndicator.show()
        })
        viewModel.error.observe(this as LifecycleOwner, { error ->
            error?.let {
                showError(error) {
                    if (isMapReady) {
                        map.clear()
                        clusterManager.clearItems()
                    }
                    viewModel.refreshData()
                }
                binding.progressIndicator.hide()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        binding.progressIndicator.show()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        viewModel.load()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        clusterManager =
            ClusterManager<ScooterClusterItem>(context, map)
        setupMap()
        isMapReady = true
        enableMyLocation()
        viewModel.data.value?.let { loadMarkers(it) }
    }

    private fun setupMap() {
        map.apply {
            isBuildingsEnabled = true
            isIndoorEnabled = true
            isTrafficEnabled = true
            uiSettings.apply {
                isZoomControlsEnabled = true
                isCompassEnabled = true
                isMyLocationButtonEnabled = true
                isScrollGesturesEnabled = true
                isZoomGesturesEnabled = true
                isTiltGesturesEnabled = true
                isRotateGesturesEnabled = true
            }
        }
    }

    private fun loadMarkers(items: List<ScooterEntity>) {
        binding.progressIndicator.show()
        if (items.isNotEmpty() && isMapReady) {
            setUpClusterer(items)
        }
        binding.progressIndicator.hide()
    }

    private fun zoomToMarkers() {
        viewModel.data.value?.let {
            if (it.isNotEmpty() && isMapReady) {
                LatLngBounds.builder().let { latLngBounds ->
                    for (scooter in it) {
                        latLngBounds.include(LatLng(scooter.latitude, scooter.longitude))
                    }
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 1))
                }
            }
        }
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_scooters_map, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_zoom_to_markers -> {
                zoomToMarkers()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun setUpClusterer(items: List<ScooterEntity>) {
        clusterManager.setOnClusterClickListener {
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    it.position,
                    floor((map.cameraPosition.zoom + 1).toDouble()).toFloat()
                ), 300, null
            )
            true
        }
        clusterManager.setOnClusterItemClickListener {
            findNavController().navigate(
                R.id.scooter_details_fragment,
                bundleOf(ScooterDetailsFragment.ARG_ID to it.scooterId)
            )
            true
        }
        clusterManager.renderer = ScooterClusterRenderer(requireContext(), map, clusterManager)

        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)

        addClusterItems(items)
    }

    private fun addClusterItems(items: List<ScooterEntity>) {
        clusterManager.clearItems()
        clusterManager.cluster()
        for (scooter in items) {
            val offsetItem =
                ScooterClusterItem(
                    scooter.latitude,
                    scooter.longitude,
                    getString(R.string.detail_title, scooter.model, scooter.fleetBirdId),
                    getString(R.string.detail_battery, scooter.battery),
                    scooter.id
                )
            clusterManager.addItem(offsetItem)
        }
        clusterManager.cluster()
        zoomToMarkers()
    }

}