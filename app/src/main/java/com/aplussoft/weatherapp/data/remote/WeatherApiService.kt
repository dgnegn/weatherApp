package com.aplussoft.weatherapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApiService {

    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String = HOURLY_PARAMS,
        @Query("daily") daily: String = DAILY_PARAMS,
        @Query("timezone") timezone: String = "auto",
    ): WeatherResponse

    companion object {
        private const val HOURLY_PARAMS =
            "temperature_2m,apparent_temperature,visibility,relative_humidity_2m,weather_code,surface_pressure,precipitation,wind_speed_10m,wind_gusts_10m,wind_direction_10m,is_day"
        private const val DAILY_PARAMS =
            "temperature_2m_max,temperature_2m_min,weather_code,sunrise,sunset"
    }
}