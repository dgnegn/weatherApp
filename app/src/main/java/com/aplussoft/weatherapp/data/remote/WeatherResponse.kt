package com.aplussoft.weatherapp.data.remote


import com.aplussoft.weatherapp.data.mapper.Daily
import com.aplussoft.weatherapp.data.mapper.Hourly
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WeatherResponse(

    @SerialName("hourly")
    val hourly: Hourly,

    @SerialName("daily")
    val daily: Daily,
)