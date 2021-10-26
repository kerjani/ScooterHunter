package com.kernacs.scooterhunter.data.repository

import com.kernacs.scooterhunter.data.entity.ScooterEntity
import com.kernacs.scooterhunter.data.entity.mappings.toEntity
import com.kernacs.scooterhunter.data.source.local.LocalDataSource
import com.kernacs.scooterhunter.data.source.remote.RemoteDataSource
import com.kernacs.scooterhunter.util.EmptyDataException
import com.kernacs.scooterhunter.util.Result
import javax.inject.Inject

class ScooterRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : Repository {

    override suspend fun getScooters(): Result<List<ScooterEntity>> =
        when (val response = remoteDataSource.getScooters()) {
            is Result.Success -> {
                if (response.data.isNullOrEmpty()) {
                    Result.Error(EmptyDataException())
                } else {
                    response.data.map { it.toEntity() }.let {
                        localDataSource.saveScooters(it)
                        Result.Success(it)
                    }
                }
            }

            is Result.Error -> {
                Result.Error(response.exception)
            }

            else -> Result.Loading
        }

    override suspend fun getScooter(id: String): Result<ScooterEntity> =
        when (val downloadedResult = localDataSource.getScooter(id)) {
            null -> Result.Error(Exception("Vehicle with id:$id has not been found!"))
            else -> Result.Success(downloadedResult)
        }

    override suspend fun saveScooters(vehicles: List<ScooterEntity>) =
        localDataSource.saveScooters(vehicles)

    override suspend fun deleteScooters() = localDataSource.deleteScooters()

}
