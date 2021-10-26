package com.kernacs.scooterhunter

import com.kernacs.scooterhunter.data.dto.ScooterResponseDto
import com.kernacs.scooterhunter.data.entity.mappings.toEntity
import com.kernacs.scooterhunter.util.EmptyDataException

val fakeScooterDto = ScooterResponseDto.Data.Current(
    id = "1",
    battery = 1,
    fleetBirdId = 1,
    latitude = 1.1,
    longitude = 1.1,
    model = "A",
    resolution = "UNKNOWN",
    state = "UNKNOWN",
    zoneId = "BUD"
)

val fakeEntity = fakeScooterDto.toEntity()

val fakeDto = ScooterResponseDto(`data` = ScooterResponseDto.Data(current = listOf(fakeScooterDto)))

val invalidDataException = EmptyDataException(0)