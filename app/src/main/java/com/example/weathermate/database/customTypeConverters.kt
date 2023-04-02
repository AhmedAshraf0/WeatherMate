package com.example.weathermate.database

import androidx.room.TypeConverter
import com.example.weathermate.weather_data_fetcher.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private val gson = Gson()

class CurrentForecastConverter {

    @TypeConverter
    fun fromJson(json: String): CurrentForecast {
        return gson.fromJson(json, CurrentForecast::class.java)
    }

    @TypeConverter
    fun toJson(forecast: CurrentForecast): String = gson.toJson(forecast)
}

class WeatherStatConverter {

    @TypeConverter
    fun fromJson(json: String): WeatherStat? {
        return gson.fromJson(json, WeatherStat::class.java)
    }

    @TypeConverter
    fun toJson(stat: WeatherStat?): String = gson.toJson(stat)
}

class CurrentWeatherListConverter {

    @TypeConverter
    fun fromJson(json: String): List<CurrentWeather> {
        // the TypeToken class, which is part of the gson
        //This object is used to get the Type object for a list of CurrentWeather objects, which is needed for the type converter.
        val type = object : TypeToken<List<CurrentWeather>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun toJson(list: List<CurrentWeather>): String = gson.toJson(list)
}

class HourlyListConverter{
    @TypeConverter
    fun fromJson(json: String) : List<HourlyForecast>{
        val type = object : TypeToken<List<HourlyForecast>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun toJson(list: List<HourlyForecast>) : String = gson.toString()
}

class TempDetailsConverter{
    @TypeConverter
    fun fromJson(json: String) : TempDetails{
        return gson.fromJson(json,TempDetails::class.java)
    }

    @TypeConverter
    fun toJson(temp: TempDetails): String = gson.toJson(temp)
}

class FeelDetailsConverter{
    @TypeConverter
    fun fromJson(json: String) : FeelLikeDetails{
        return gson.fromJson(json,FeelLikeDetails::class.java)
    }

    @TypeConverter
    fun toJson(feel: FeelLikeDetails): String = gson.toJson(feel)
}

class DailyListConverter{
    @TypeConverter
    fun fromJson(json: String) : List<DailyForecast>{
        val type = object : TypeToken<List<DailyForecast>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun toJson(list: List<DailyForecast>) : String = gson.toString()
}