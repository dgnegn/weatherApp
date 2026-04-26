package com.aplussoft.weatherapp.data.mapper

import com.aplussoft.weatherapp.data.remote.WeatherResponse

import com.aplussoft.weatherapp.domain.model.DailyForecast
import com.aplussoft.weatherapp.domain.model.HourlyForecast
import com.aplussoft.weatherapp.domain.model.WeatherInfo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun WeatherResponse.toWeatherInfo(): WeatherInfo {

    val hourlyForecasts = hourly.time.mapIndexed { index, time ->
        HourlyForecast(
            time = time?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) } ?: LocalDateTime.now(),
            temperature = hourly.temperature_2m[index],
            apparentTemperature = hourly.apparent_temperature[index],
            visibility = hourly.visibility[index],
            relativeHumidity = hourly.relative_humidity_2m[index],
            weatherCode = WeatherType.fromWmoCode(hourly.weather_code[index]),
            surfacePressure = hourly.surface_pressure[index],
            precipitation = hourly.precipitation[index],
            windSpeed = hourly.wind_speed_10m[index],
            windGusts = hourly.wind_gusts_10m[index],
            windDirection = hourly.wind_direction_10m[index],
            isDay = hourly.is_day[index]
        )
    }

    val dailyForecasts = daily.time.mapIndexed { index, time ->
        DailyForecast(
            time = time?.let { LocalDate.parse(it, DateTimeFormatter.ISO_DATE) } ?: LocalDate.now(),
            temperatureMin = daily.temperature_2m_min[index],
            temperatureMax = daily.temperature_2m_max[index],
            weatherCode = WeatherType.fromWmoCode(daily.weather_code[index]),
            sunrise = daily.sunrise[index],
            sunset = daily.sunset[index]
        )
    }

    return WeatherInfo(
        hourlyForecast = hourlyForecasts,
        dailyForecast = dailyForecasts,
    )
}
