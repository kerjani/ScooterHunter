package com.kernacs.scooterhunter.data.entity.mappings

import com.kernacs.scooterhunter.data.dto.ScooterResponseDto
import com.kernacs.scooterhunter.data.entity.Resolution
import com.kernacs.scooterhunter.data.entity.State

fun ScooterResponseDto.Data.Current.toEntity() =
    com.kernacs.scooterhunter.data.entity.ScooterEntity(
        id = id,
        battery = battery,
        fleetBirdId = fleetBirdId,
        latitude = latitude,
        longitude = longitude,
        model = model,
        resolution = Resolution.fromString(resolution),
        state = State.fromString(state),
        zoneId = zoneId
    )