package com.kernacs.scooterhunter.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scooters")
data class ScooterEntity(
    @PrimaryKey
    val id: String,
    val battery: Int,
    val fleetBirdId: Int,
    val latitude: Double,
    val longitude: Double,
    val model: String,
    val resolution: Resolution? = null,
    val state: State,
    val zoneId: String
)

enum class Resolution(val value: String) {
    UNKNOWN("UNKNOWN"),
    CLAIMED("CLAIMED"),
    NOT_FOUND("NOT_FOUND"),
    OTHER("OTHER");

    companion object {
        private val map = values().associateBy(Resolution::value)
        fun fromString(type: String?) = map[type] ?: UNKNOWN
    }
}

enum class State(val value: String) {
    UNKNOWN("UNKNOWN"),
    GPS_ISSUE("GPS_ISSUE"),
    ACTIVE("ACTIVE"),
    LOST("LOST"),
    LOW_BATTERY("LOW_BATTERY"),
    MAINTENANCE("LOW_BATTERY"),
    LAST_SEARCH("LAST_SEARCH"),
    DAMAGED("DAMAGED"),
    OUT_OF_ORDER("DAMAGED");

    companion object {
        private val map = values().associateBy(State::value)
        fun fromString(type: String?) = map[type] ?: State.UNKNOWN
    }
}
