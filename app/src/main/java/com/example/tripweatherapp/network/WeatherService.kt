package com.example.tripweatherapp.network

import com.example.tripweatherapp.model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

// 배포시에는 안전하게 보완 적용 필요
// 공공 데이터 포탈에서 발급 받은 자신만의 API키를 입력해 주세요.
private const val API_KEY =
    "GWffBPTtiI6KgaJWMjwJAxWPZqm1NW1Zg%2Fh0SR3AGMeN%2F0Fw7O%2BsuOI31qlFFzaqBSjxuFv2CMdrnqWFK7CoTw%3D%3D"

interface WeatherService {

    @GET("getVilageFcst?serviceKey=$API_KEY")
    suspend fun getWeather(
        @Query("base_date") baseDate: Int,
        @Query("base_time") baseTime: String = "0500",
        @Query("nx") nx: String = "60",
        @Query("ny") ny: String = "121",
        @Query("numOfRows") numOfRows: Int = 12,
        @Query("pageNo") pageNo: Int = 1,
        @Query("dataType") dataType: String = "JSON"
    ): WeatherModel
}