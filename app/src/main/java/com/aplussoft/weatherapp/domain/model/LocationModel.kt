package com.aplussoft.weatherapp.domain.model

data class LocationModel(

    val id: Long,

    val name: String,

    val latitude: Double,

    val longitude: Double,

    val elevation: Double,

    val featureCode: String,

    val countryCode: String,

    val timezone: String,

    val population: Int,

    val postcodes: List<String> = emptyList(),

    val countryId: Long,

    val country: String,

    val admin1: String,

    val admin2: String,

    val admin3: String? = null,

    val admin4: String? = null,
)
