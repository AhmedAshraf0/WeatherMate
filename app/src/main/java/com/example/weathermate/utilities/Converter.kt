package com.example.weathermate.utilities

import androidx.databinding.InverseMethod
import java.text.NumberFormat
import java.util.*

object Converter {
    @InverseMethod("convertStringToInt")
    fun convertIntToString(value: Int): String {
        val locale = Locale.getDefault()
        val isArabic = locale.language == "ar"
        val numberFormat = if (isArabic) {
            NumberFormat.getNumberInstance(Locale("ar"))
        } else {
            NumberFormat.getNumberInstance()
        }
        return numberFormat.format(value)
    }

    fun convertStringToInt(value: String): Int {
        return try{
            value.toInt()
        }   catch (e: NumberFormatException) {
            0
        }
    }

    @InverseMethod("convertStringToIntDouble")
    fun convertDoubleToIntString(value: Double): String {
        val locale = Locale.getDefault()
        val isArabic = locale.language == "ar"
        val numberFormat = if (isArabic) {
            NumberFormat.getNumberInstance(Locale("ar"))
        } else {
            NumberFormat.getNumberInstance()
        }
        return numberFormat.format(value.toInt()).toString()
    }

    fun convertStringToIntDouble(value: String): Double {
        return try {
            value.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }
    @InverseMethod("convertStringToDouble")
    fun convertDoubleToString(value: Double): String {
        val locale = Locale.getDefault()
        val isArabic = locale.language == "ar"
        val numberFormat = if (isArabic) {
            NumberFormat.getNumberInstance(Locale("ar"))
        } else {
            NumberFormat.getNumberInstance()
        }
        return numberFormat.format(value).toString()
    }

    fun convertStringToDouble(value: String): Double {
        return try {
            value.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }
}