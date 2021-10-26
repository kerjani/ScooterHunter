package com.kernacs.scooterhunter.ui.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.kernacs.scooterhunter.R

class ScooterClusterRenderer(
    val context: Context,
    map: GoogleMap?,
    clusterManager: ClusterManager<ScooterClusterItem>?
) : DefaultClusterRenderer<ScooterClusterItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(
        item: ScooterClusterItem,
        markerOptions: MarkerOptions
    ) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.scooter_marker))
    }

}