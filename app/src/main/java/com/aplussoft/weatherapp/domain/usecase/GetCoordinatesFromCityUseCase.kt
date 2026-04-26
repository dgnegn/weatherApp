package com.aplussoft.weatherapp.domain.usecase

import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.aplussoft.weatherapp.core.util.Result
import com.aplussoft.weatherapp.di.IoDispatcher
import com.aplussoft.weatherapp.domain.model.Coordinates
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume

class GetCoordinatesFromCityUseCase @Inject constructor(
    private val geocoder: Geocoder,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(cityName: String): Result<Coordinates, Error> {
        return withContext(dispatcher) {
            try {
                val addresses = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    suspendCancellableCoroutine<List<Address>?> { continuation ->
                        geocoder.getFromLocationName(cityName, 1) { addresses ->
                            continuation.resume(addresses)
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    geocoder.getFromLocationName(cityName, 1)
                }

                if (addresses.isNullOrEmpty()) {
                    return@withContext Result.Failure(Error("Location not found"))
                }
                val location = addresses.firstOrNull()
                if (location != null) {
                    Result.Success(Coordinates(location.latitude, location.longitude))

                } else {
                    Result.Failure(Error("Location not found"))
                }
            } catch (_: IOException) {
                Result.Failure(Error("Network error"))
            } catch (_: IllegalArgumentException) {
                Result.Failure(Error("Location not found"))
            } catch (e: Exception) {
                Result.Failure(Error(e.message ?: "Unknown error"))
            }
        }
    }
}
