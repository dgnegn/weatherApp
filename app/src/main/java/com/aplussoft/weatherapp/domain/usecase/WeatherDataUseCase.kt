package com.aplussoft.weatherapp.domain.usecase


import com.aplussoft.weatherapp.core.util.Result
import com.aplussoft.weatherapp.domain.model.WeatherInfo
import com.aplussoft.weatherapp.domain.repository.WeatherRepository
import java.io.IOException
import javax.inject.Inject

class WeatherDataUseCase @Inject constructor(private val repository: WeatherRepository) {

    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
    ): Result<WeatherInfo, Error> {
        return try {
            repository.getWeatherData(latitude, longitude)
        } catch (e: Exception) {
            Result.Failure(Error("Unknown error: ${e.message}"))
        } catch (e: IOException) {
            Result.Failure(Error("Network error: ${e.message}"))
        }

    }

}
