package com.example.forecastapp.mvvm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecastapp.MyApplication
import com.example.forecastapp.RetrofitInstance
import com.example.forecastapp.SharedPrefs
import com.example.forecastapp.modal.WeatherList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherVm:ViewModel() {


    val todayWeatherLiveData = MutableLiveData<List<WeatherList>>()

    val forecastWeatherLiveData = MutableLiveData<List<WeatherList>>()

    val closeorExactlySameWeatherLData = MutableLiveData<List<WeatherList>>()



    val cityName = MutableLiveData<String>()
    val context = MyApplication.instance

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeather(city:String?=null) = viewModelScope.launch(Dispatchers.IO) {


        val todayWeatherList = mutableListOf<WeatherList>()
        val currentDateTime = LocalDateTime.now()
        val currentDatePattern = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val sharedPrefs = SharedPrefs.getInstance(context)
        val lat = sharedPrefs.getValue("lat").toString()
        val lon = sharedPrefs.getValue("lon").toString()

        val call = if(city!=null){
            RetrofitInstance.api.getWeatherByCity(city)

        }else{
            RetrofitInstance.api.getCurrentWeather(lat,lon)
        }


        val response = call.execute()
        if(response.isSuccessful){
            val weatherList = response.body()?.list
            cityName.postValue(response.body()?.city!!.name)

            val presentDate = currentDatePattern


            weatherList?.forEach{weather ->
                if (weather.dt_txt!!.split("\\s".toRegex()).contains(presentDate)){
                    todayWeatherList.add(weather)

                }
            }

            //if the api time is cosest the system!s time display that
            val closestWeather = findCloseWeather(todayWeatherList)
            closestWeather?.let {
                closeorExactlySameWeatherLData.postValue(listOf(it))
            }

            todayWeatherLiveData.postValue(todayWeatherList)


        }

    }









    @RequiresApi(Build.VERSION_CODES.O)
    fun getForecastUpcoming(city:String?=null) = viewModelScope.launch(Dispatchers.IO) {


        val forecastWeatherList = mutableListOf<WeatherList>()
        val currentDateTime = LocalDateTime.now()
        val currentDatePattern = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val sharedPrefs = SharedPrefs.getInstance(context)
        val lat = sharedPrefs.getValue("lat").toString()
        val lon = sharedPrefs.getValue("lon").toString()

        val call = if(city!=null){
            RetrofitInstance.api.getWeatherByCity(city)

        }else{
            RetrofitInstance.api.getCurrentWeather(lat,lon)
        }


        val response = call.execute()
        if(response.isSuccessful){
            val weatherList = response.body()?.list
            cityName.postValue(response.body()?.city!!.name)

            val presentDate = currentDatePattern


            weatherList?.forEach{weather ->
                if (!weather.dt_txt!!.split("\\s".toRegex()).contains(presentDate)){
                  if (weather.dt_txt!!.substring(11,16)=="12:00"){
                      forecastWeatherList.add(weather)
                  }



                }
            }


            forecastWeatherLiveData.postValue(forecastWeatherList)


        }

    }



















    @RequiresApi(Build.VERSION_CODES.O)
        private fun findCloseWeather(weatherList:List<WeatherList>): WeatherList? {
            val systemTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        var closestWeather:WeatherList?=null
        var minTimeDifference = Int.MAX_VALUE




        for (weather in weatherList) {
            val weatherTime = weather.dt_txt!!.substring(11, 16)
            val timeDifference = Math.abs(timeToMinutes(weatherTime)-timeToMinutes(systemTime))
            if (timeDifference < minTimeDifference) {
                minTimeDifference = timeDifference
                        closestWeather= weather
            }
        }



        return closestWeather

    }



    private fun timeToMinutes(time:String):Int{

        val parts = time.split(":")
        return parts[0].toInt()*60+parts[1].toInt()

    }








}