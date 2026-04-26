package com.aplussoft.weatherapp.domain.usecase


import android.util.Log
import com.aplussoft.weatherapp.core.util.Result
import com.aplussoft.weatherapp.domain.model.LocationModel


import com.aplussoft.weatherapp.domain.repository.WeatherRepository

import javax.inject.Inject

class GetLocationUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(cityName: String): Result<List<LocationModel>, Error> {
        Log.d("GetLocationUseCase", "Invoking with cityName: $cityName")
        val locationData = weatherRepository.getLocationData(cityName)
        Log.d("GetLocationUseCase", "Location data: $locationData")
        return locationData
    }
}
