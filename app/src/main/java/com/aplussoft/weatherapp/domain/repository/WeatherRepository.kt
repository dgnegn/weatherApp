package com.aplussoft.weatherapp.domain.repository




import com.aplussoft.weatherapp.core.util.Result

import com.aplussoft.weatherapp.domain.model.LocationModel
import com.aplussoft.weatherapp.domain.model.WeatherInfo


interface WeatherRepository {

    suspend fun getLocationData(name: String): Result<List<LocationModel>, Error>

    suspend fun getWeatherData(lat: Double, long: Double): Result<WeatherInfo, Error>


}


