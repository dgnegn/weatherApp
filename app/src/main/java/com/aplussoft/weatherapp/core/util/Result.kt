package com.aplussoft.weatherapp.core.util


sealed interface Result<out D, out E>   {
    data object Loading : Result<Nothing, Nothing>
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Failure<out E>(val error: E) : Result<Nothing, E>
}
