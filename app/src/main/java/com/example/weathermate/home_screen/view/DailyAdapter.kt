package com.example.weathermate.home_screen.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermate.R
import com.example.weathermate.databinding.CardDayBinding
import com.example.weathermate.home_screen.model.photos
import com.example.weathermate.utilities.Converter
import com.example.weathermate.weather_data_fetcher.DailyForecast
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter : ListAdapter<DailyForecast, DailyAdapter.ViewHolder>(DiffUtilDaily()) {

    inner class ViewHolder(var cardDayBinding: CardDayBinding) :
        RecyclerView.ViewHolder(cardDayBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.card_day,
            parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDay = getItem(position)
        holder.cardDayBinding.dayDate.text = convertToDate(currentDay.time)
        holder.cardDayBinding.currentDay.text = convertToDayName(currentDay.time)
        holder.cardDayBinding.dayStatus.setImageResource(photos.get(currentDay.weather.get(0).icon)!!)
        holder.cardDayBinding.maxTemp.text = Converter.convertDoubleToIntString(currentDay.temp.max)
        holder.cardDayBinding.minTemp.text = Converter.convertDoubleToIntString(currentDay.temp.min)

    }

    private fun convertToDate(timestamp: Long) : String{
        val sdf = SimpleDateFormat("dd-MM", Locale.getDefault())
        val date = Date(timestamp * 1000L)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }
    private fun convertToDayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEE", Locale.getDefault())
        val date = Date(timestamp * 1000L)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

}