package com.aplussoft.weatherapp.domain.model

import com.aplussoft.weatherapp.data.mapper.WeatherType
import java.time.LocalDateTime

data class HourlyForecast(
    val time: LocalDateTime,
    val temperature: Double,
    val apparentTemperature: Double,
    val visibility: Double,
    val relativeHumidity: Double,
    val weatherCode: WeatherType,
    val surfacePressure: Double,
    val precipitation: Double,
    val windSpeed: Double,
    val windGusts: Double,
    val windDirection: Double,
    val isDay: Int
)
