package com.example.weathermate.home_screen.viewmodel

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("convertToDate")
fun convertToDate(view : TextView , timestamp: Long) {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = Date(timestamp * 1000L)
    sdf.timeZone = TimeZone.getDefault()
    view.text = sdf.format(date)
}

@BindingAdapter("convertToTime")
fun convertToTime(view : TextView ,timestamp: Long) {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val date = Date(timestamp * 1000L)
    sdf.timeZone = TimeZone.getDefault()
    view.text = sdf.format(date)
}