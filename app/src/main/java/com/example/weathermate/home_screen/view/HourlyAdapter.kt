package com.example.weathermate.home_screen.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermate.R
import com.example.weathermate.databinding.CardHourlyBinding
import com.example.weathermate.home_screen.model.photos
import com.example.weathermate.utilities.Converter
import com.example.weathermate.weather_data_fetcher.HourlyForecast
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter() : ListAdapter<HourlyForecast, HourlyAdapter.ViewHolder>(DiffUtilHourly()) {

    inner class ViewHolder(var cardHourlyBinding: CardHourlyBinding) :
        RecyclerView.ViewHolder(cardHourlyBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.card_hourly,
            parent, false
        )
    )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentHour = getItem(position)

        holder.cardHourlyBinding.currentImg.setImageResource(photos.get(currentHour.weather.get(0).icon)!!)
        holder.cardHourlyBinding.currentHour.text = convertToTime(currentHour.time)
        holder.cardHourlyBinding.currentDayDeg.text = Converter.convertDoubleToIntString(currentHour.temp)
    }

    private fun convertToTime(timestamp: Long): String {
        //hh -> 12-hour format
        //HH -> 24-hour format
        //a  -> PM - AM
        //aa  -> pm - am
        val sdf = SimpleDateFormat("hh a", Locale.getDefault())
        val date = Date(timestamp * 1000L)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }
}