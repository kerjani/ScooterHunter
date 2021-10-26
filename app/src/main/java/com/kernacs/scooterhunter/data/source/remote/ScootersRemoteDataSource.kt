package com.kernacs.scooterhunter.data.source.remote

import android.util.Log
import com.kernacs.scooterhunter.newtwork.ScooterApi
import com.kernacs.scooterhunter.util.Result
import javax.inject.Inject

class ScootersRemoteDataSource @Inject constructor(
    private val api: ScooterApi
) : RemoteDataSource {
    override suspend fun getScooters() = try {
        val result = api.downloadScooters()
        Result.Success(result.data.current)
    } catch (exception: Exception) {
        Log.e("ScootersRemoteDataSource", exception.stackTraceToString())
        Result.Error(exception)
    }

}
