package com.kernacs.scooterhunter.data.repository

import com.kernacs.scooterhunter.data.entity.ScooterEntity
import com.kernacs.scooterhunter.util.Result

interface Repository {
    suspend fun getScooters(): Result<List<ScooterEntity>>
    suspend fun getScooter(id: String): Result<ScooterEntity>
    suspend fun saveScooters(vehicles: List<ScooterEntity>)
    suspend fun deleteScooters()
}