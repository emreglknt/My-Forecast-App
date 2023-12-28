package com.example.forecastapp.modal

data class ForeCast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: ArrayList<WeatherList>,
    val message: Int
)