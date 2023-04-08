package com.example.weathermate.favorites.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermate.R
import com.example.weathermate.databinding.CardFavoriteBinding
import com.example.weathermate.favorites.viewmodel.FavoriteViewModel
import com.example.weathermate.home_screen.model.photos
import com.example.weathermate.utilities.Converter
import com.example.weathermate.weather_data_fetcher.FavoriteWeatherResponse
import java.text.SimpleDateFormat
import java.util.*

class FavoritesAdapter(
    private val favoriteViewModel: FavoriteViewModel,
    private val context: Context
) : ListAdapter<FavoriteWeatherResponse, FavoritesAdapter.ViewHolder>(DiffUtilFavorites()) {

    inner class ViewHolder(var cardFavoriteBinding: CardFavoriteBinding) :
        RecyclerView.ViewHolder(cardFavoriteBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapter.ViewHolder =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.card_favorite,
                parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentFavCard = getItem(position)
        holder.cardFavoriteBinding.tvLastTime.text = convertToTime(currentFavCard.dt)
        holder.cardFavoriteBinding.tvFavDeg.text =
            Converter.convertDoubleToIntString(currentFavCard.temp)
        holder.cardFavoriteBinding.favImg.setImageResource(photos.get(currentFavCard.img)!!)
        holder.cardFavoriteBinding.tvCityName.text = currentFavCard.cityName
        holder.cardFavoriteBinding.tvWeatherDesc.text = currentFavCard.description
        holder.cardFavoriteBinding.deleteIcon.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes") { _, _ ->
                    favoriteViewModel.deleteFavorite(currentFavCard)
                }
                .setNegativeButton("Cancel", null)
                .show()

        }
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