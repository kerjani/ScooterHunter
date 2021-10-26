package com.kernacs.scooterhunter.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kernacs.scooterhunter.data.entity.ScooterEntity

@Dao
interface ScootersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(scooters: List<ScooterEntity>)

    @Query("SELECT * FROM scooters")
    suspend fun getScooters(): List<ScooterEntity>

    @Query("SELECT * FROM scooters WHERE id=:id")
    suspend fun getScooterBId(id: String): ScooterEntity?

    @Query("DELETE FROM scooters")
    suspend fun deleteAll()
}