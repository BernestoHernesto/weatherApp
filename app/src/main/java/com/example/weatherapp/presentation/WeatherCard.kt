package com.example.weatherapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.domain.model.WeatherType
import com.example.weatherapp.domain.model.WeatherData

@Composable
fun WeatherCard(
    state: WeatherData?,
    modifier: Modifier = Modifier
) {

    Card(
        colors = CardDefaults.cardColors(),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            state?.current?.weatherType?.let {
                Image(
                    painter = painterResource(id = getDrawableResourceForWeatherType(it)),
                    contentDescription = null,
                    modifier = Modifier.size(
                        width = 90.dp, height = 90.dp
                    )
                )
            }
            Spacer(modifier = Modifier.width(32.dp))
            Row {
                Text(
                    text = "${state?.current?.temperature}",
                    fontSize = 50.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "${state?.currentUnits?.temperature}",
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.weather_humidity),
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${state?.current?.humidity}",
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Light,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${state?.currentUnits?.humidity}",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Light,
            )
        }
    }
}

fun getDrawableResourceForWeatherType(weatherType: WeatherType): Int {
    return when (weatherType) {
        WeatherType.CLEAR_SKY -> R.drawable.ic_sunny
        WeatherType.CLOUDY -> R.drawable.ic_cloudy
        WeatherType.DRIZZLE -> R.drawable.ic_rainy
        WeatherType.RAIN -> R.drawable.ic_rainy
        WeatherType.SNOW -> R.drawable.ic_snowy
        WeatherType.THUNDER -> R.drawable.ic_thunder
    }
}
