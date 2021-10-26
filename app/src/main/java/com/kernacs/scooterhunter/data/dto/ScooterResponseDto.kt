package com.kernacs.scooterhunter.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScooterResponseDto(
    val `data`: Data
) {
    @Serializable
    data class Data(
        val current: List<Current>,
    ) {
        @Serializable
        data class Current(
            val id: String,
            val battery: Int,
            @SerialName(value = "fleetbirdId")
            val fleetBirdId: Int,
            val latitude: Double,
            val longitude: Double,
            val model: String,
            val resolution: String? = null,
            val state: String,
            val zoneId: String,
        )
    }
}