package com.example.weathermate.alerts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weathermate.R
import com.example.weathermate.databinding.FragmentAlertsBinding
import com.example.weathermate.dialog.MyDialogFragment


class AlertsFragment : Fragment() {
    private lateinit var _binding : FragmentAlertsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlertsBinding.inflate(inflater)
        _binding.myFragment = this

        return _binding.root
    }

    fun onFabClicked(view : View){
        val alertsDialogFragment = AlertsDialogFragment()
        alertsDialogFragment.show(parentFragmentManager, "MyDialogFragment")
    }
}