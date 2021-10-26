package com.kernacs.scooterhunter.data.source.local

import com.kernacs.scooterhunter.data.entity.ScooterEntity
import javax.inject.Inject

class ScootersLocalDataSource @Inject constructor(
    private val dao: ScootersDao
) : LocalDataSource {
    override suspend fun getScooters(): List<ScooterEntity> = dao.getScooters()

    override suspend fun getScooter(id: String): ScooterEntity? = dao.getScooterBId(id)

    override suspend fun saveScooters(items: List<ScooterEntity>) = dao.insertAll(items)

    override suspend fun deleteScooters() = dao.deleteAll()
}
