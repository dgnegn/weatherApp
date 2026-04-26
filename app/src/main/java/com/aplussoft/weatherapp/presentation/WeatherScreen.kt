package com.aplussoft.weatherapp.presentation


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aplussoft.weatherapp.R
import com.aplussoft.weatherapp.core.ui.AnimatedBackground
import com.aplussoft.weatherapp.core.ui.AppTheme
import com.aplussoft.weatherapp.core.util.Result
import com.aplussoft.weatherapp.core.util.WindDirection
import com.aplussoft.weatherapp.domain.model.DailyForecast
import com.aplussoft.weatherapp.domain.model.HourlyForecast
import com.aplussoft.weatherapp.domain.model.WeatherInfo
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.ZoneId
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    state: WeatherUiState,
    actions: (WeatherAction) -> Unit,
    ) {

    val focusManager = LocalFocusManager.current

    val isDay = when (val weatherResult = state.weatherResult) {
        is Result.Success -> {
            val currentHour = now().hour
            val currentWeather = weatherResult.data.hourlyForecast.getOrNull(currentHour)
                ?: weatherResult.data.hourlyForecast.firstOrNull()
            currentWeather?.isDay == 1
        }

        else -> true
    }

    BackHandler(
        onBack = {
            when {
                state.isSearchActive -> actions(WeatherAction.OnClearSearch)
                state.isPrivacy -> actions(WeatherAction.OnPrivacy)
                state.isMenuOpen -> actions(WeatherAction.OnMenuClicked)
                else -> {
                    actions(WeatherAction.OnExit)
                }
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedBackground(isDay = isDay)
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                if (state.isPrivacy) {
                    TopAppBar(
                        colors = topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = Color.White
                        ),
                        title = { Text(stringResource(R.string.privacy_policy_title)) },
                        navigationIcon = {
                            IconButton(onClick = {
                                actions(WeatherAction.OnPrivacy)

                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.content_desc_back),
                                    tint = Color.White
                                )
                            }
                        }
                    )
                } else if (!state.isSearchActive) {
                    TopAppBar(
                        colors = topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = Color.White
                        ),
                        title = {
                            if (state.isConnected && !state.isLoading) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = stringResource(R.string.content_desc_location),
                                        tint = Color.White
                                    )
                                    Text(
                                        text = state.currentLocation?.replaceFirstChar { it.uppercase() }
                                            ?: stringResource(R.string.select_location),
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else if (!state.isConnected) {
                                Text(stringResource(R.string.app_name))
                            }
                        },
                        actions = {
                            if (state.isConnected && !state.isLoading) {
                                IconButton(
                                    onClick = {
                                        actions(WeatherAction.OnSearchActive)
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = stringResource(R.string.content_desc_search),
                                        tint = Color.White
                                    )
                                }

                                Box {
                                    IconButton(
                                        onClick = { actions(WeatherAction.OnMenuClicked) },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = stringResource(R.string.content_desc_more),
                                            tint = Color.White
                                        )
                                    }
                                    DropdownMenu(
                                        expanded =  state.isMenuOpen,
                                        onDismissRequest = { actions(WeatherAction.OnMenuClicked) },
                                        shape = ShapeDefaults.Large,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                            .background(Color.White.copy(alpha = 0.1f))

                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    stringResource(R.string.menu_privacy),
                                                    color = Color.Black
                                                )
                                            },
                                            onClick = {
                                                actions(WeatherAction.OnMenuClicked)
                                                actions(WeatherAction.OnPrivacy)
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.PrivacyTip,
                                                    contentDescription = null
                                                )
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    stringResource(R.string.menu_exit),
                                                    color = Color.Black
                                                )
                                            },
                                            onClick = {
                                                actions(WeatherAction.OnMenuClicked)
                                                actions(WeatherAction.OnExit)
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                                    contentDescription = null
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            },

            ) { padding ->
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    if (state.isPrivacy) {
                        PrivacyScreen()
                    } else {
                        PullToRefreshBox(
                            isRefreshing = state.isLoading,
                            onRefresh = { actions(WeatherAction.OnRefresh) },
                            state = rememberPullToRefreshState(),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Main Content
                            when (val weatherResult = state.weatherResult) {
                                is Result.Loading -> LoadingContent(modifier = Modifier.fillMaxSize())
                                is Result.Failure -> ShowError(
                                    message = weatherResult.error?.message.toString(),
                                    modifier = Modifier.fillMaxSize(),
                                    uiState = state,
                                    event = actions

                                )

                                is Result.Success -> {
                                    WeatherDetails(
                                        state = weatherResult.data,
                                        uiState = state,
                                        event = actions
                                    )
                                }

                                else -> {}
                            }
                        }
                    }
                }

                // Search Overlay
                if (state.isSearchActive && !state.isPrivacy) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.9f))
                            .clickable(enabled = true, onClick = {
                                focusManager.clearFocus()
                                actions(WeatherAction.OnClearSearch)
                            })
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        WeatherTopBar(
                            state = state,
                            event = actions
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun WeatherTopBar(
    state: WeatherUiState,
    event: (WeatherAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val locationList = state.locationList ?: emptyList()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = true, onClick = {}), // Prevent clicks from dismissing overlay
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.1f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.White.copy(alpha = 0.5f),
                unfocusedIndicatorColor = Color.White.copy(alpha = 0.2f),
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.fillMaxWidth(),
            value = state.searchQuery,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                showKeyboardOnFocus = true
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    event(WeatherAction.OnSearchClicked)
                    focusManager.clearFocus()
                }
            ),
            onValueChange = {
                event(WeatherAction.OnSearchQueryChanged(it))
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.search_location),
                    color = Color.White.copy(alpha = 0.6f)
                )
            },
            leadingIcon = {
                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        event(WeatherAction.OnClearSearch)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.content_desc_back),
                        tint = Color.White
                    )
                }
            },
            trailingIcon = {
                if (state.searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            event(WeatherAction.OnSearchQueryChanged(""))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.content_desc_clear),
                            tint = Color.White
                        )
                    }
                }
            },
        )

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp),
                color = Color.White
            )
        }

        if (locationList.isNotEmpty() && !state.isLoading) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.DarkGray.copy(alpha = 0.9f)
                )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    items(locationList) { location ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    event(WeatherAction.OnLocationSelected(location))
                                    focusManager.clearFocus()
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = location.name,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = location.country,
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun WeatherDetails(
    state: WeatherInfo,
    uiState: WeatherUiState,
    event: (WeatherAction) -> Unit,
) {
    val today = now().atZone(ZoneId.systemDefault()).toLocalDate()
    val todayForecast = state.dailyForecast.find { it.time == today }
    val sunRise = todayForecast?.sunrise?.let {
        try {
            LocalDateTime.parse(it).toLocalTime().toString()
        } catch (_: Exception) {
            null
        }
    } ?: "N/A"

    val sunSet = todayForecast?.sunset?.let {
        try {
            LocalDateTime.parse(it).toLocalTime().toString()
        } catch (_: Exception) {
            null
        }
    } ?: "N/A"

    val currentHour = now().hour
    val currentWeather =
        state.hourlyForecast.getOrNull(currentHour) ?: state.hourlyForecast.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        CurrentWeatherCard(
            currentWeather = currentWeather,
            sunRise = sunRise,
            sunSet = sunSet
        )

        Spacer(modifier = Modifier.height(16.dp))

        HourlyWeatherForecast(state.hourlyForecast, state = uiState, event = event)

        Spacer(modifier = Modifier.height(16.dp))

        SevenDaysWeatherForecast(
            item = state.dailyForecast,
            state = uiState,
            event = event
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun CurrentWeatherCard(
    currentWeather: HourlyForecast,
    sunRise: String?,
    sunSet: String?,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            painter = painterResource(id = currentWeather.weatherCode.iconRes),
            contentDescription = null,
            modifier = Modifier.size(95.dp),
            tint = Color.White.copy(alpha = 0.4f)
        )

        Text(
            text = "${currentWeather.temperature.toInt()}°",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 80.sp,
                fontWeight = FontWeight.W200
            ),
            color = Color.White
        )

        Text(
            text = stringResource(id = currentWeather.weatherCode.descriptionRes),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White.copy(alpha = 0.8f)
        )

        Text(
            text = stringResource(R.string.real_feel, currentWeather.apparentTemperature.toInt()),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)

        ) {
            WeatherDetailItem(
                icon = Icons.Default.WaterDrop,
                label = stringResource(R.string.humidity),
                value = "${currentWeather.relativeHumidity.toInt()}%",
                modifier = Modifier.weight(1f)
            )
            val direction = WindDirection.fromDegree(currentWeather.windDirection)
            val windSpeedAndDirection = "${currentWeather.windSpeed} km/h ${direction.name}"
            WeatherDetailItem(
                icon = Icons.Default.Air,
                label = stringResource(R.string.wind),
                value = windSpeedAndDirection,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WeatherDetailItem(
                icon = painterResource(id = R.drawable.pressure),
                label = stringResource(R.string.pressure),
                value = "${currentWeather.surfacePressure} hPa",
                modifier = Modifier.weight(1f)
            )
            WeatherDetailItem(
                icon = Icons.Default.WbSunny,
                label = stringResource(R.string.sun),
                value = "$sunRise - $sunSet",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun HourlyWeatherForecast(
    data: List<HourlyForecast>,
    state: WeatherUiState,
    event: (WeatherAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_weather_clear),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.hourly_forecast),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            IconButton(onClick = { event(WeatherAction.OnHourlyForecastClicked) }) {
                Icon(
                    imageVector = if (state.isHourlyForecastShown) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.content_desc_toggle),
                    tint = Color.White
                )
            }
        }

        if (state.isHourlyForecastShown)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                val currentTime = now()
                val validData = data.filter { it.time >= currentTime.minusHours(1) }.take(24)

                items(validData, key = { it.time.toString() }) { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .background(
                                color = if (item.time.hour == currentTime.hour) Color.White.copy(
                                    alpha = 0.1f
                                ) else Color.Transparent, shape = MaterialTheme.shapes.medium
                            )
                            .padding(4.dp)


                    ) {
                        Text(
                            text = if (item.time.hour == currentTime.hour) stringResource(R.string.now) else "${item.time.hour}:00",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Icon(
                            painter = painterResource(id = item.weatherCode.iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = Color.White.copy(alpha = 0.4f)
                        )
                        Text(
                            text = "${item.temperature.toInt()}°",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

    }
}

@Composable
fun SevenDaysWeatherForecast(
    item: List<DailyForecast>,
    state: WeatherUiState,
    event: (WeatherAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_weather_cloudy),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string._7_day_forecast),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
            IconButton(onClick = { event(WeatherAction.OnDailyForecastClicked) }) {
                Icon(
                    imageVector = if (state.isDailyForecastShown) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.content_desc_toggle),
                    tint = Color.White
                )
            }
        }
        if (state.isDailyForecastShown)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item.forEach { dailyItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)

                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.weight(0.4f)

                        ) {
                            Text(
                                text = dailyItem.time?.dayOfWeek?.name?.lowercase(Locale.getDefault())
                                    ?.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.getDefault()
                                        ) else it.toString()
                                    } ?: stringResource(R.string.unknown),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White,

                                )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.weight(0.4f)

                        ) {
                            Icon(
                                painter = painterResource(id = dailyItem.weatherCode.iconRes),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.White.copy(alpha = 0.4f)
                            )

                            Text(
                                text = stringResource(id = dailyItem.weatherCode.descriptionRes),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.4f),

                                )

                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.weight(0.2f)

                        ) {
                            Text(
                                text = "${dailyItem.temperatureMax.toInt()}°/",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = "${dailyItem.temperatureMin.toInt()}°",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.4f)
                            )
                        }
                    }
                }
            }

    }
}

@Composable
fun WeatherDetailItem(
    icon: Any,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                when (icon) {
                    is ImageVector -> Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )

                    is Painter -> Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.6f),

                    )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun ShowError(
    message: String,
    modifier: Modifier = Modifier,
    uiState: WeatherUiState,
    event: (WeatherAction) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.1f),
                contentColor = Color.White
            ),
            onClick = {
                event(WeatherAction.OnLoadData(uiState.locationName!!))
            },

            ) {
            Text(text = stringResource(R.string.retry))
        }
    }
}


@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun PrivacyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = stringResource(R.string.privacy_intro),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f)
        )

        val sections = listOf(
            R.string.privacy_collection_title to R.string.privacy_collection_desc,
            R.string.privacy_use_title to R.string.privacy_use_desc,
            R.string.privacy_disclosure_title to R.string.privacy_disclosure_desc,
            R.string.privacy_tracking_title to R.string.privacy_tracking_desc,
            R.string.privacy_security_title to R.string.privacy_security_desc,
            R.string.privacy_children_title to R.string.privacy_children_desc,
            R.string.privacy_rights_title to R.string.privacy_rights_desc,
            R.string.privacy_changes_title to R.string.privacy_changes_desc,
            R.string.privacy_contact_title to R.string.privacy_contact_desc
        )

        sections.forEach { (titleRes, contentRes) ->
            PrivacySection(
                title = stringResource(titleRes),
                content = stringResource(contentRes)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.privacy_last_updated),
                color = Color.White.copy(alpha = 0.5f),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun PrivacySection(title: String, content: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

@Preview
@Composable
private fun PrivacyScreenPreview() {
    AppTheme {

        Box {
            AnimatedBackground(isDay = false)
            PrivacyScreen()
        }
    }
}

@Preview
@Composable
private fun WeatherTopBarPreview() {
    AppTheme {
        Box(modifier = Modifier.background(Color.Black)) {
            WeatherTopBar(
                state = WeatherUiState(
                    searchQuery = "Ankara",
                    isSearchActive = true
                ),
                event = {}
            )
        }
    }
}
