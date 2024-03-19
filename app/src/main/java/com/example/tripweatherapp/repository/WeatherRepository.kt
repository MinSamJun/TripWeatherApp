package com.example.tripweatherapp.repository

import com.example.tripweatherapp.data.cityList
import com.example.tripweatherapp.model.WeatherModel
import com.example.tripweatherapp.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository {
    suspend fun getWeather(date: String, cityName: String, pageNo: Int = 1): WeatherModel {
        return withContext(Dispatchers.IO) {
            val city = cityList.find { it.name == cityName } ?: cityList.first()
            RetrofitInstance.service.getWeather(
                baseDate = date.toInt(),
                nx = city.nx.toString(),
                ny = city.ny.toString(),
                numOfRows = pageNo * 12
            )
        }
    }
}