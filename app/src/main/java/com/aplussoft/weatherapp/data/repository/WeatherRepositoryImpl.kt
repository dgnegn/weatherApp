package com.aplussoft.weatherapp.data.repository


import com.aplussoft.weatherapp.core.util.Result
import com.aplussoft.weatherapp.data.mapper.toLocationModel
import com.aplussoft.weatherapp.data.mapper.toWeatherInfo
import com.aplussoft.weatherapp.data.remote.LocationApiService
import com.aplussoft.weatherapp.data.remote.WeatherApiService
import com.aplussoft.weatherapp.domain.model.LocationModel
import com.aplussoft.weatherapp.domain.model.WeatherInfo
import com.aplussoft.weatherapp.domain.repository.WeatherRepository
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApiService,
    private val locationApi: LocationApiService,
) : WeatherRepository {
    override suspend fun getLocationData(name: String): Result<List<LocationModel>, Error> {
        return try {
            val response = locationApi.getLocations(name)
            Result.Success(response.results.map { it.toLocationModel()})
        } catch (e: Exception) {
            Result.Failure(Error("Unknown error"))
        }
    }


    override suspend fun getWeatherData(
        lat: Double,
        long: Double,
    ): Result<WeatherInfo, Error> {
        return try {
            val response = api.getWeather(lat, long)
            Result.Success(response.toWeatherInfo())
        } catch (_: IOException) {
            Result.Failure(Error("Network error"))
        } catch (_: Exception) {
            Result.Failure(Error("Unknown error"))
        }
    }

}
