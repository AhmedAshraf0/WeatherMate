package com.example.weathermate.home_screen.viewmodel

import androidx.databinding.InverseMethod
import java.text.SimpleDateFormat
import java.util.*

object Converter {
    @InverseMethod("convertStringToInt")
    fun convertIntToString(value: Int): String {
        return value.toString()
    }

    fun convertStringToInt(value: String): Int {
        return try{
            value.toInt()
        }   catch (e: NumberFormatException) {
            0
        }
    }

    @InverseMethod("convertStringToDouble")
    fun convertDoubleToString(value: Double): String {
        return value.toString()
    }

    fun convertStringToDouble(value: String): Double {
        return try {
            value.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }
}