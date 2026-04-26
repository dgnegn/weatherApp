package com.aplussoft.weatherapp.data.remote

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters


import com.aplussoft.weatherapp.core.util.SavedLocationSharedPreferences
import com.aplussoft.weatherapp.domain.repository.WeatherRepository
import com.aplussoft.weatherapp.domain.usecase.GetCoordinatesFromCityUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WeatherUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: WeatherRepository,
    private val getCoordinatesUseCase: GetCoordinatesFromCityUseCase,
    private val sharedPreferences: SavedLocationSharedPreferences
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val location = sharedPreferences.getLocation() ?: return Result.success()

        return try {
            when (val cordsResult = getCoordinatesUseCase(location)) {
                is com.aplussoft.weatherapp.core.util.Result.Success -> {
                    val weatherResult = repository.getWeatherData(
                        cordsResult.data.latitude,
                        cordsResult.data.longitude
                    )
                    
                    if (weatherResult is com.aplussoft.weatherapp.core.util.Result.Success) {
                        Result.success()
                    } else {
                        Result.retry()
                    }
                }
                is com.aplussoft.weatherapp.core.util.Result.Failure -> {
                    if (cordsResult.error == Error("Network error")) {
                        Result.retry()
                    } else {
                        Result.failure()
                    }
                }
                else -> Result.retry()
            }
        } catch (e: Exception) {
            Log.e("WeatherUpdateWorker", "Error updating weather", e)
            Result.retry()
        }
    }
}
