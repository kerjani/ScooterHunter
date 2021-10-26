package com.kernacs.scooterhunter.data.source.local

import com.kernacs.scooterhunter.data.entity.ScooterEntity

interface LocalDataSource {
    suspend fun getScooters(): List<ScooterEntity>
    suspend fun getScooter(id: String): ScooterEntity?
    suspend fun saveScooters(items: List<ScooterEntity>)
    suspend fun deleteScooters()
}