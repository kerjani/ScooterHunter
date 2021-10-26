package com.kernacs.scooterhunter.newtwork

import com.kernacs.scooterhunter.data.dto.ScooterResponseDto
import com.kernacs.scooterhunter.util.Constants
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject

class ScooterApi @Inject constructor(private val httpClient: HttpClient) {

    suspend fun downloadScooters() =
        httpClient.get<ScooterResponseDto>(Constants.BASE_URL)
}