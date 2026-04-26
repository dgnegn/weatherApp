package com.aplussoft.weatherapp.core

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aplussoft.weatherapp.core.ui.AppTheme
import com.aplussoft.weatherapp.presentation.WeatherScreen
import com.aplussoft.weatherapp.presentation.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<WeatherViewModel>()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Schedule the background updates for the first time
        viewModel.scheduleWeatherUpdates(applicationContext)

        // change the status bar's color to transparent and icons' to white
        enableEdgeToEdge(
            SystemBarStyle.dark(
                Color.TRANSPARENT
            )
        )

        setContent {
            AppTheme {
                val state = viewModel.uiState.collectAsStateWithLifecycle()
                LaunchedEffect(state.value.isExit) {
                    if (state.value.isExit) {
                        finishAndRemoveTask()
                    }
                }

                WeatherScreen(
                    actions = viewModel::onWeatherAction,
                    state = state.value
                )
            }
        }
    }
}
