package com.example.basemvpapp

/**
 * @author: mlsnatalie
 * @date: 2020/6/29 4:35 PM
 * @UpdateDate: 2020/6/29 4:35 PM
 * email：mlsnatalie@163.com
 * description：
 */
data class WhetherData(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Bean>,
    val message: Double
)

data class City(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val timezone: Int
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class Bean(
    val clouds: Int,
    val deg: Int,
    val dt: Int,
    val feels_like: FeelsLike,
    val humidity: Int,
    val pressure: Int,
    val speed: Double,
    val sunrise: Int,
    val sunset: Int,
    val temp: Temp,
    val weather: List<Weather>
)

data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
)

data class Temp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
)

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)