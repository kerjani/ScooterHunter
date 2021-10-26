package com.kernacs.scooterhunter.ui.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class ScooterClusterItem(
    val lat: Double,
    val lng: Double,
    val itemTitle: String? = null,
    val snippetText: String? = null,
    val scooterId: String
) : ClusterItem {

    override fun getPosition() = LatLng(lat, lng)

    override fun getTitle() = itemTitle

    override fun getSnippet() = snippetText
}