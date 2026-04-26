package com.aplussoft.weatherapp.domain.model

import com.aplussoft.weatherapp.data.mapper.WeatherType
import java.time.LocalDate

data class DailyForecast(
    val time: LocalDate?,
    val temperatureMin: Double,
    val temperatureMax: Double,
    val weatherCode: WeatherType,
    val sunrise: String,
    val sunset: String
)
