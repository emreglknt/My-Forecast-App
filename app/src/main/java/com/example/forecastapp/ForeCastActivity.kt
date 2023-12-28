package com.example.forecastapp

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.forecastapp.adapter.ForeCastAdapter
import com.example.forecastapp.modal.WeatherList
import com.example.forecastapp.mvvm.WeatherVm

class ForeCastActivity : AppCompatActivity() {


    private lateinit var adapterForeCastAdapter: ForeCastAdapter
    lateinit var vIm:WeatherVm
    lateinit var rvForeCast : RecyclerView




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fore_cast)

        vIm = ViewModelProvider(this).get(WeatherVm::class.java)

        adapterForeCastAdapter = ForeCastAdapter()

        rvForeCast = findViewById<RecyclerView>(R.id.forecastRv)



        val sharedPrefs = SharedPrefs.getInstance(this)

        val city = sharedPrefs.getValueOrNull("city")



        if(city!=null){
            vIm.getForecastUpcoming(city)

        }else{
            vIm.getForecastUpcoming()
        }




        vIm.forecastWeatherLiveData.observe(this, Observer {

            val setNewList = it as List<WeatherList>


            Log.d("forecast LiveData",setNewList.toString())


            adapterForeCastAdapter.setList(setNewList)

            rvForeCast.adapter= adapterForeCastAdapter


        })



    }
}