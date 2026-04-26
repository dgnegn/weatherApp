package com.aplussoft.weatherapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApiService {
    @GET("v1/search")
    suspend fun getLocations(
        @Query("name") name: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "en",
        @Query("format") format: String = "json",
    ): LocationResponse
}
