package com.example.weathermate.home_screen.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weathermate.databinding.FragmentHomeBinding
import com.example.weathermate.home_screen.model.HomeRepository
import com.example.weathermate.home_screen.viewmodel.HomeViewModel
import com.example.weathermate.home_screen.viewmodel.HomeViewModelFactory
import com.example.weathermate.network.ApiState
import com.example.weathermate.network.ConcreteRemoteSource
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var _binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var factory: HomeViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        factory = HomeViewModelFactory(
            HomeRepository.getInstance(
                ConcreteRemoteSource()
            ),
            35.393528,
            -119.043732,
            "metric",
            "en"
        )
        homeViewModel =
            ViewModelProvider(this,factory).get(HomeViewModel::class.java)

        lifecycleScope.launchWhenCreated {
            homeViewModel.responseStateFlow.collectLatest {
                when(it){
                    is ApiState.Success ->{
                        Log.i(TAG, "collectLatest: ${it.data}")
                    }
                    else -> {
                        Log.i(TAG, "collectLatest: sad")
                    }
                }
            }
        }

        return  _binding.root
    }

}