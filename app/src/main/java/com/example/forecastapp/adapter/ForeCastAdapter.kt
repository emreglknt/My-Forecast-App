package com.example.forecastapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forecastapp.R
import com.example.forecastapp.modal.WeatherList
import java.text.SimpleDateFormat
import java.util.Locale

class ForeCastAdapter : RecyclerView.Adapter<ForeCastHolder>() {


    private var listForeCast = listOf<WeatherList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForeCastHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.upcomingforecastlist,parent,false)

        return ForeCastHolder(view)
    }

    override fun onBindViewHolder(holder: ForeCastHolder, position: Int) {
        val forecastObject = listForeCast[position]
        for (i in forecastObject.weather){
            holder.description.text = i.description!!
        }


        holder.humidity.text = forecastObject.main!!.humidity.toString()
        holder.windspeed.text = forecastObject.wind?.speed.toString()


        val temperatureFahrenheit=forecastObject.main?.temp
        val temperatureCelsius =  (temperatureFahrenheit?.minus(273.15))
        val temperatureFormatted= String.format("%.2f", temperatureCelsius)
        holder.temp.text = "$temperatureFormatted C"



        val inputFormat = SimpleDateFormat("yyy-MM-dd HH:mm", Locale.getDefault())
        val date= inputFormat.parse(forecastObject.dt_txt!!)
        val outputformat = SimpleDateFormat("d MMMM EEEE", Locale.getDefault())
        val dateanddayname = outputformat.format(date!!)

        holder.dateDayName.text = dateanddayname


        for(i in forecastObject.weather){
            if(i.icon == "01d"){
                holder.imageGraphic.setImageResource(R.drawable.oned)
                holder.smallicon.setImageResource(R.drawable.oned)
            }

            if(i.icon == "01n"){
                holder.imageGraphic.setImageResource(R.drawable.onen)
                holder.smallicon.setImageResource(R.drawable.onen)
            }

            if(i.icon == "02d"){
                holder.imageGraphic.setImageResource(R.drawable.twod)
                holder.smallicon.setImageResource(R.drawable.twod)
            }


            if(i.icon == "02n"){
                holder.imageGraphic.setImageResource(R.drawable.twon)
                holder.smallicon.setImageResource(R.drawable.twon)
            }



            if(i.icon == "03d"||i.icon == "03n"){
                holder.imageGraphic.setImageResource(R.drawable.threedn)
                holder.smallicon.setImageResource(R.drawable.threedn)
            }

            if(i.icon == "10d"){
                holder.imageGraphic.setImageResource(R.drawable.tend)
                holder.smallicon.setImageResource(R.drawable.tend)
            }


            if(i.icon == "04d"||i.icon == "04n"){
                holder.imageGraphic.setImageResource(R.drawable.fourdn)
                holder.imageGraphic.setImageResource(R.drawable.fourdn)
            }


            if(i.icon == "09d"||i.icon == "09n"){
                holder.imageGraphic.setImageResource(R.drawable.ninedn)
                holder.imageGraphic.setImageResource(R.drawable.ninedn)
            }

            if(i.icon == "11d"||i.icon == "11n"){
                holder.imageGraphic.setImageResource(R.drawable.elevend)
                holder.imageGraphic.setImageResource(R.drawable.elevend)
            }


            if(i.icon == "13d"||i.icon == "13n"){
                holder.imageGraphic.setImageResource(R.drawable.thirteend)
                holder.imageGraphic.setImageResource(R.drawable.thirteend)
            }

            if(i.icon == "50d"||i.icon == "50n"){
                holder.imageGraphic.setImageResource(R.drawable.fiftydn)
                holder.imageGraphic.setImageResource(R.drawable.fiftydn)
            }
        }


    }

    override fun getItemCount(): Int {
      return listForeCast.size
    }


    fun setList(newlist:List<WeatherList>){
        this.listForeCast = newlist
    }




}


        class ForeCastHolder(itemView:View): RecyclerView.ViewHolder(itemView){

            val imageGraphic: ImageView = itemView.findViewById(R.id.imageGraphic)
            val description: TextView = itemView.findViewById(R.id.weatherDescr)
            val humidity:TextView = itemView.findViewById(R.id.humidity)
            val windspeed : TextView = itemView.findViewById(R.id.windSpeed)
            val temp: TextView = itemView.findViewById(R.id.tempDisplayForeCast)
            val smallicon: ImageView = itemView.findViewById(R.id.smallicon)
            val dateDayName: TextView = itemView.findViewById(R.id.dayDateText)

        }