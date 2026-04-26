package com.aplussoft.weatherapp.data.remote

import com.aplussoft.weatherapp.data.mapper.WeatherLocation
import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    val results: List<WeatherLocation> = emptyList(),
)
