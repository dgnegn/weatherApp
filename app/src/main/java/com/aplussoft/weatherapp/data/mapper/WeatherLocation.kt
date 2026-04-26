package com.aplussoft.weatherapp.data.mapper

import com.aplussoft.weatherapp.domain.model.LocationModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.Long

@Serializable
data class WeatherLocation(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("elevation")
    val elevation: Double? = null,
    @SerialName("feature_code")
    val featureCode: String? = null,
    @SerialName("country_code")
    val countryCode: String? = null,
    @SerialName("admin1_id")
    val admin1Id: Long? = null,
    @SerialName("admin2_id")
    val admin2Id: Long? = null,
    @SerialName("admin3_id")
    val admin3Id: Long? = null,
    @SerialName("admin4_id")
    val admin4Id: Long? = null,
    @SerialName("timezone")
    val timezone: String? = null,
    @SerialName("population")
    val population: Int? = null,
    @SerialName("postcodes")
    val postcodes: List<String>? = emptyList(),
    @SerialName("country_id")
    val countryId: Long? = null,
    @SerialName("country")
    val country: String? = null,
    @SerialName("admin1")
    val admin1: String? = null,
    @SerialName("admin2")
    val admin2: String? = null,
    @SerialName("admin3")
    val admin3: String? = null,
    @SerialName("admin4")
    val admin4: String? = null,
)

fun WeatherLocation.toLocationModel(): LocationModel {
    return LocationModel(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        elevation = elevation ?: 0.0,
        featureCode = featureCode ?: "",
        countryCode = countryCode ?: "",
        timezone = timezone ?: "",
        population = population ?: 0,
        postcodes = postcodes ?: emptyList(),
        countryId = countryId ?: 0,
        country = country ?: "",
        admin1 = admin1 ?: "",
        admin2 = admin2 ?: "",
        admin3 = admin3 ?: "",
        admin4 = admin4 ?: "",
    )
}
