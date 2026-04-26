package com.aplussoft.weatherapp.data.mapper

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Hourly(
    @SerialName("time")
    val time: List<String?> = emptyList(),

    @SerialName("temperature_2m")
    val temperature_2m: List<Double> = emptyList(),

    @SerialName("apparent_temperature")
    val apparent_temperature: List<Double> = emptyList(),

    @SerialName("visibility")
    val visibility: List<Double> = emptyList(),

    @SerialName("relative_humidity_2m")
    val relative_humidity_2m: List<Double> = emptyList(),

    @SerialName("weather_code")
    val weather_code: List<Int> = emptyList(),

    @SerialName("surface_pressure")
    val surface_pressure: List<Double> = emptyList(),

    @SerialName("precipitation")
    val precipitation: List<Double> = emptyList(),

    @SerialName("wind_speed_10m")
    val wind_speed_10m: List<Double> = emptyList(),

    @SerialName("wind_gusts_10m")
    val wind_gusts_10m: List<Double> = emptyList(),

    @SerialName("wind_direction_10m")
    val wind_direction_10m: List<Double> = emptyList(),

    @SerialName("is_day")
    val is_day: List<Int> = emptyList(),
)
