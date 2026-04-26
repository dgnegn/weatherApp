package com.aplussoft.weatherapp.data.mapper

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.aplussoft.weatherapp.R

enum class WeatherType(
    @param:StringRes val descriptionRes: Int,
    @param:DrawableRes val iconRes: Int,
) {
    ClearSky(R.string.weather_clear, R.drawable.ic_weather_clear),
    Cloudy(R.string.weather_cloudy, R.drawable.ic_weather_cloudy),
    Foggy(R.string.weather_foggy, R.drawable.ic_weather_foggy),
    Rainy(R.string.weather_rainy, R.drawable.ic_weather_rainy),
    Snowy(R.string.weather_snowy, R.drawable.ic_weather_snowy),
    Thunderstorm(R.string.weather_thunder, R.drawable.ic_weather_thunder),
    Unknown(R.string.weather_unknown, R.drawable.ic_weather_unknown);

    companion object {
        fun fromWmoCode(code: Int): WeatherType {
            return when (code) {
                0 -> ClearSky
                1, 2, 3 -> Cloudy
                45, 48 -> Foggy
                // Drizzle, Rain, Freezing Rain, Rain Showers
                51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82 -> Rainy
                // Snow fall, Snow grains, Snow showers
                71, 73, 75, 77, 85, 86 -> Snowy
                // Thunderstorm
                95, 96, 99 -> Thunderstorm
                else -> Unknown
            }
        }
    }
}
