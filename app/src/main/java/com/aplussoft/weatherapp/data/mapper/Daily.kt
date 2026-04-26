package com.aplussoft.weatherapp.data.mapper

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Daily(
    @SerialName("time")
    val time: List<String?>,
    @SerialName("temperature_2m_min")
    val temperature_2m_min: List<Double>,
    @SerialName("temperature_2m_max")
    val temperature_2m_max: List<Double>,
    @SerialName("weather_code")
    val weather_code: List<Int>,
    @SerialName("sunrise")
    val sunrise: List<String>,
    @SerialName("sunset")
    val sunset: List<String>,
)