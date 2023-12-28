package com.example.forecastapp

import com.example.forecastapp.modal.City
import com.example.forecastapp.modal.ForeCast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("forecast?")
    fun getCurrentWeather(
        @Query("lat")
        lat:String,
        @Query("lon")
        lon:String,
        @Query("appid")
        appid:String=Utils.API_KEY
    ):Call<ForeCast>


    @GET("forecast?")
    fun getWeatherByCity(
        @Query("q")
        city: String,
        @Query("appid")
        appid:String=Utils.API_KEY
    ):Call<ForeCast>




}