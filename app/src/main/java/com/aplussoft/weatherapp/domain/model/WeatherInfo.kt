package com.aplussoft.weatherapp.domain.model



data class WeatherInfo (
    val hourlyForecast: List<HourlyForecast>,
    val dailyForecast: List<DailyForecast>,
)

