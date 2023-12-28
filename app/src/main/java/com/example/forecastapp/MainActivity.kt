package com.example.forecastapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.DatabaseUtils
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.forecastapp.adapter.ForeCastAdapter
import com.example.forecastapp.adapter.WeatherToday
import com.example.forecastapp.databinding.ActivityMainBinding
import com.example.forecastapp.modal.WeatherList
import com.example.forecastapp.mvvm.WeatherVm
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var vm : WeatherVm
    private lateinit var adapter: WeatherToday


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
        vm = ViewModelProvider(this).get(WeatherVm::class.java)

        adapter = WeatherToday()


        binding.lifecycleOwner=this
        binding.vm = vm


        vm.getWeather()

        //when app starts clear the city we had searched previously
        val sharedPrefs = SharedPrefs.getInstance(this@MainActivity)
        sharedPrefs.clearCityValue()

        vm.todayWeatherLiveData.observe(this, Observer {


            val setNewList = it as List<WeatherList>

            adapter.setList(setNewList)
            binding.forecastRv.adapter = adapter


        })




        vm.closeorExactlySameWeatherLData.observe(this, Observer { weatherList ->

            weatherList?.let {
                for (weatherItem in it) {
                    val temperatureFahrenheit = weatherItem.main.temp
                    val temperatureCelsius = temperatureFahrenheit?.minus(273.15)
                    val temperatureFormatted = String.format("%.2f", temperatureCelsius)

                    for (i in weatherItem.weather.orEmpty()) {
                        binding.descMain.text = i.description
                    }

                    binding.tempMain.text = "$temperatureFormatted °C"
                    binding.humidity.text = weatherItem.main.humidity.toString()
                    binding.windSpeed.text = weatherItem.wind.speed.toString()

                    val inputFormat = SimpleDateFormat("yyy-MM-dd HH:mm", Locale.getDefault())
                    val date = inputFormat.parse(weatherItem.dt_txt)
                    val outputFormat = SimpleDateFormat("d MMMM EEEE", Locale.getDefault())
                    val dateAndDayName = outputFormat.format(date!!)

                    binding.dateDayMain.text = dateAndDayName
                    binding.chanceofrain.text = "${weatherItem.pop.toString()}%"

                    for (i in weatherItem.weather.orEmpty()) {
                        if (i.icon == "01d") {
                            binding.ImageMain.setImageResource(R.drawable.oned)
                        }
                        if(i.icon == "01n"){
                            binding.ImageMain.setImageResource(R.drawable.onen)
                        }

                        if(i.icon == "02d"){
                            binding.ImageMain.setImageResource(R.drawable.twod)
                        }


                        if(i.icon == "02n"){
                            binding.ImageMain.setImageResource(R.drawable.twon)
                        }



                        if(i.icon == "03d"||i.icon == "03n"){
                            binding.ImageMain.setImageResource(R.drawable.threedn)
                        }

                        if(i.icon == "10d"){
                            binding.ImageMain.setImageResource(R.drawable.tend)
                        }


                        if(i.icon == "04d"||i.icon == "04n"){
                            binding.ImageMain.setImageResource(R.drawable.fourdn)
                        }


                        if(i.icon == "09d"||i.icon == "09n"){
                            binding.ImageMain.setImageResource(R.drawable.ninedn)
                        }

                        if(i.icon == "11d"||i.icon == "11n"){
                            binding.ImageMain.setImageResource(R.drawable.elevend)
                        }


                        if(i.icon == "13d"||i.icon == "13n"){
                            binding.ImageMain.setImageResource(R.drawable.thirteend)
                        }

                        if(i.icon == "50d"||i.icon == "50n"){
                            binding.ImageMain.setImageResource(R.drawable.fiftydn)
                        }

                    }
                }
            }
        })


        if(checkLocationPermissions()){
            getCurrentLocation()
        }   else{
            requestLocationPermissions()

        }



        val searchEditText = binding.searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
    searchEditText.setTextColor(Color.WHITE)


        binding.next5Days.setOnClickListener{
            startActivity(Intent(this,ForeCastActivity::class.java))
        }

        binding.searchView.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{


            override fun onQueryTextSubmit(query: String?): Boolean {
                val sharedPrefs = SharedPrefs.getInstance(this@MainActivity)
                sharedPrefs.setValueOrNull("city",query!!)

                if (!query.isNullOrEmpty()){
                    vm.getWeather(query)
                    binding.cityName.text=query
                    binding.searchView.setQuery("",false)
                    binding.searchView.clearFocus()
                    binding.searchView.isIconified = true

                }
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                    return true
            }


        })






    }


        private fun checkLocationPermissions():Boolean{
            val fineLocationPermission = ContextCompat.checkSelfPermission(
                this,android.Manifest.permission.ACCESS_FINE_LOCATION
            )

            val coarseLocationPermission=ContextCompat.checkSelfPermission(
                this,android.Manifest.permission.ACCESS_COARSE_LOCATION
            )


            return fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                    coarseLocationPermission == PackageManager.PERMISSION_GRANTED


        }


        private fun  requestLocationPermissions(){

            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                Utils.PERMISSION_REQUEST_CODE
                )

        }


        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            if(requestCode == Utils.PERMISSION_REQUEST_CODE){
                if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED&&
                    grantResults[1]==PackageManager.PERMISSION_GRANTED){

                    getCurrentLocation()

                }else{
                    //permission denied
                }
            }

       }



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("ServiceCast")
    private fun getCurrentLocation(){
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(ActivityCompat.checkSelfPermission(
                this,  android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&

            ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            ==PackageManager.PERMISSION_GRANTED
        ){
            val location : Location? =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location!=null){
                val latitude = location.latitude
                val longitude = location.longitude

                val myprefs = SharedPrefs.getInstance(this@MainActivity)
                myprefs.setValue("lon",longitude.toString())
                myprefs.setValue("lat",latitude.toString())
            }
        }
    }


}