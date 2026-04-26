package com.aplussoft.weatherapp.core.util

import android.content.SharedPreferences
import androidx.core.content.edit
import jakarta.inject.Inject

class SavedLocationSharedPreferences @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveLocation(location: String) {
        sharedPreferences.edit {
            putString("location", location)
        }
    }

    fun getLocation(): String? {
        return sharedPreferences.getString("location", "Ankara")
    }

    fun saveLatitude(latitude: String) {
        sharedPreferences.edit {
            putString("latitude", latitude)
        }
    }

    fun getLatitude(): String? {
        return sharedPreferences.getString("latitude", "39.9199")
    }

    fun saveLongitude(longitude: String) {
        sharedPreferences.edit {
            putString("longitude", longitude)
        }
    }

    fun getLongitude(): String? {
        return sharedPreferences.getString("longitude", "32.8543")
    }
}