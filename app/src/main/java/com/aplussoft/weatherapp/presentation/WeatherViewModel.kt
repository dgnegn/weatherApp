package com.aplussoft.weatherapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.aplussoft.weatherapp.WeatherApplication
import com.aplussoft.weatherapp.core.util.ConnectivityObserver
import com.aplussoft.weatherapp.core.util.Result
import com.aplussoft.weatherapp.core.util.SavedLocationSharedPreferences
import com.aplussoft.weatherapp.data.remote.WeatherUpdateWorker

import com.aplussoft.weatherapp.domain.model.LocationModel
import com.aplussoft.weatherapp.domain.model.WeatherInfo
import com.aplussoft.weatherapp.domain.usecase.GetLocationUseCase
import com.aplussoft.weatherapp.domain.usecase.WeatherDataUseCase
import com.aplussoft.weatherapp.presentation.WeatherAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


data class WeatherUiState(
    val isLoading: Boolean = false,
    val weatherInfo: WeatherInfo? = null,
    val error: String? = null,
    val latitude: Double? = 39.9199,
    val longitude: Double? = 32.8543,
    val locationName: String? = "Ankara",
    val currentLocation: String? = null,
    val showDialog: Boolean = false,
    val isSearchActive: Boolean = false,
    val isHourlyForecastShown: Boolean = true,
    val isDailyForecastShown: Boolean = false,
    val weatherResult: Result<WeatherInfo, Error?>? = null,
    val searchQuery: String = "",
    val locationResult: Result<List<LocationModel>, Error?>? = null,
    val locationList: List<LocationModel>? = null,
    val isConnected: Boolean = false,
    val isUpdate: Boolean = false,
    val isPrivacy: Boolean = false,
    val isExit: Boolean = false,
    val isMenuOpen: Boolean = false
)

sealed interface WeatherAction {
    object IsLoading : WeatherAction
    data class OnLoadData(val locationName: String) : WeatherAction
    data class OnSearchQueryChanged(val newName: String) : WeatherAction
    object OnSearchClicked : WeatherAction
    object OnHourlyForecastClicked : WeatherAction
    object OnDailyForecastClicked : WeatherAction
    object OnSearchActive : WeatherAction
    object OnClearSearch : WeatherAction
    data class OnLocationSelected(val location: LocationModel) : WeatherAction
    object OnConnected  : WeatherAction
    object OnRefresh : WeatherAction
    object OnUpdate : WeatherAction
    object OnPrivacy : WeatherAction
    object OnExit : WeatherAction
    object OnMenuClicked : WeatherAction


}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherDataUseCase: WeatherDataUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val connectivityObserver: ConnectivityObserver,
    private val sharedPreferences: SavedLocationSharedPreferences,
    @ApplicationContext private val context: android.content.Context
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState = _uiState.asStateFlow()
    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    latitude = sharedPreferences.getLatitude()?.toDouble() ?: it.latitude,
                    longitude = sharedPreferences.getLongitude()?.toDouble() ?: it.longitude,
                    currentLocation = sharedPreferences.getLocation(),
                    locationName = sharedPreferences.getLocation() ?: it.locationName
                )
            }
            fetchWeatherForCity(_uiState.value.locationName!!.lowercase())
        }
        isInternetAvailable()
    }

    private fun isInternetAvailable() {
        if (!connectivityObserver.isConnected()) {
            _uiState.update {
                it.copy(
                    weatherResult = Result.Failure(Error("No internet connection")),
                    isConnected = false
                )
            }
            return
        }
    }

    fun onWeatherAction(event: WeatherAction) {
        when (event) {
            is OnLoadData -> {
                fetchWeatherForCity(event.locationName)
            }

            is OnSearchQueryChanged -> {
                _uiState.update { state ->
                    state.copy(
                        searchQuery = event.newName,
                    )
                }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    if (event.newName.length < 2) {
                        _uiState.update { state ->
                            state.copy(
                                locationList = emptyList(),
                                isLoading = false
                            )
                        }
                        return@launch
                    }
                    delay(500)
                    _uiState.update { it.copy(isLoading = true) }
                    val result = getLocationUseCase(event.newName)
                    Log.d("WeatherViewModel", "Search query changed: ${event.newName}")
                    _uiState.update { state ->
                        state.copy(
                            locationList = if (result is Result.Success) result.data else emptyList(),
                            isLoading = false
                        )
                    }
                    searchJob?.cancel()
                    searchJob = null
                    Log.d("WeatherViewModel", "Search query changed: ${_uiState.value.locationList?.toList()}")


                }

            }

            is OnSearchClicked -> {
                val selectedLocation =
                    _uiState.value.locationList?.firstOrNull { it.name == _uiState.value.searchQuery }

                if (selectedLocation != null) {
                    onWeatherAction(OnLocationSelected(selectedLocation))
                }
            }

            OnDailyForecastClicked -> {
                _uiState.update { it.copy(isDailyForecastShown = !it.isDailyForecastShown) }
            }

            OnHourlyForecastClicked -> {
                _uiState.update { it.copy(isHourlyForecastShown = !it.isHourlyForecastShown) }
            }

            IsLoading -> {
                _uiState.update { it.copy(isLoading = true) }
            }

            OnSearchActive -> {
                _uiState.update {
                    it.copy(
                        isSearchActive = !it.isSearchActive,
                        searchQuery = ""
                    )
                }
            }

            OnClearSearch -> {
                _uiState.update {
                    it.copy(
                        searchQuery = "",
                        isSearchActive = false,
                        locationList = emptyList()
                    )
                }
            }

            is OnLocationSelected -> {
                _uiState.update {
                    it.copy(
                        locationName = event.location.name,
                        currentLocation = event.location.name,
                        latitude = event.location.latitude,
                        longitude = event.location.longitude,
                        isSearchActive = false,
                        searchQuery = "",
                        locationList = emptyList(),
                        isLoading = false
                    )
                }
                fetchWeatherForCity(
                    event.location.name,
                    event.location.latitude,
                    event.location.longitude
                )
            }

            OnConnected -> {
                _uiState.update { it.copy(isConnected = !it.isConnected, isLoading = true) }
            }

            OnRefresh -> {
                _uiState.value.currentLocation?.let {
                    fetchWeatherForCity(it)
                }
            }

            OnUpdate -> {
                _uiState.value.currentLocation?.let {
                    fetchWeatherForCity(it)
                }
            }

            OnPrivacy -> {
                _uiState.update { it.copy(isPrivacy = !it.isPrivacy) }
            }

            OnExit -> {
                _uiState.update { it.copy(
                    isPrivacy = false,
                    isMenuOpen = false,
                    isExit = true,
                ) }
            }

            OnMenuClicked -> {
                _uiState.update { it.copy(isMenuOpen = !it.isMenuOpen) }
            }
        }

    }

    fun fetchWeatherForCity(cityName: String, lat: Double? = null, lon: Double? = null) {
        // Check internet first
        if (!connectivityObserver.isConnected()) {
            _uiState.update {
                it.copy(
                    weatherResult = Result.Failure(Error("No internet connection")),
                )
            }
            return
        }
        if (cityName.isBlank()) {
            _uiState.update {
                it.copy(
                    weatherResult = Result.Failure(Error("City name cannot be empty")),
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    weatherResult = Result.Loading,
                    isSearchActive = false,
                )
            }

            val finalLat = lat ?: _uiState.value.latitude
            val finalLon = lon ?: _uiState.value.longitude

            if (finalLat != null && finalLon != null) {
                fetchWeatherDetails(finalLat, finalLon)
                sharedPreferences.saveLocation(cityName)
                sharedPreferences.saveLatitude(finalLat.toString())
                sharedPreferences.saveLongitude(finalLon.toString())
                _uiState.update {
                    it.copy(
                        currentLocation = cityName,
                        locationName = cityName,
                        latitude = finalLat,
                        longitude = finalLon,
                        isLoading = false,
                        isConnected =  true

                    )
                }
            } else {
                _uiState.update {
                    it.copy(weatherResult = Result.Failure(Error("Location not found")))
                }

            }
        }
    }

    private suspend fun fetchWeatherDetails(lat: Double, lon: Double) {
        val result = weatherDataUseCase(latitude = lat, longitude = lon)
        _uiState.update {
            it.copy(
                weatherResult = result,
            )
        }
    }


    fun scheduleWeatherUpdates(androidContext: android.content.Context) {
        val constraints = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Only run when connected to internet
            .setRequiresBatteryNotLow(true)
            .build()

        val weatherRequest = PeriodicWorkRequestBuilder<WeatherUpdateWorker>(
            1, TimeUnit.HOURS // Repeat every 1 hour
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(androidContext).enqueueUniquePeriodicWork(
            "WeatherUpdateWork",
            ExistingPeriodicWorkPolicy.KEEP, // Keep existing if already scheduled
            weatherRequest
        )
    }
}
