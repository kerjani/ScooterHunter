package com.kernacs.scooterhunter.data.source.remote

import com.kernacs.scooterhunter.data.dto.ScooterResponseDto
import com.kernacs.scooterhunter.util.Result

interface RemoteDataSource {
    suspend fun getScooters(): Result<List<ScooterResponseDto.Data.Current>>
}