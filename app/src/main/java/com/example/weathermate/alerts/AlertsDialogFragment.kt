package com.example.weathermate.alerts

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.weathermate.R
import com.example.weathermate.databinding.FragmentAlertsDialogBinding
import java.text.SimpleDateFormat
import java.util.*


class AlertsDialogFragment : DialogFragment() {
    private val TAG = "AlertsDialogFragment"
    private lateinit var _binding: FragmentAlertsDialogBinding
    private val calendar: Calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private val hour = calendar.get(Calendar.HOUR_OF_DAY)
    private val minute = calendar.get(Calendar.MINUTE)
    private var currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    private var LimitDate = currentDate
    private var currentTime = "$hour:$minute"
    private lateinit var alertTypes : Array<String>
    var isAlarm : Boolean? = null
    var selectTime = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlertsDialogBinding.inflate(inflater)
        _binding.myFragment = this

        alertTypes = resources.getStringArray(R.array.alert_types)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.tvCurrentDate.text = currentDate

        _binding.tvLimitDate.text = currentDate

        _binding.tvCurrentTime.text = currentTime

        _binding.dropMenu.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.alert_types),
        )

        _binding.dropMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.i(TAG, "onItemSelected: ${alertTypes.get(position)}")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        _binding.tvCurrentDate.setOnClickListener {
            showDatePicker(it as TextView)
        }

        _binding.tvLimitDate.setOnClickListener {
            showDatePicker(it as TextView)
        }

        _binding.tvCurrentTime.setOnClickListener {
            showTimePicker(it as TextView)
        }

    }

    private fun showDatePicker(textView: TextView) {
        val selectedDate = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
            selectedDate.set(year, month, day)

            if(textView.id == _binding.tvCurrentDate.id){
                currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
                textView.text = currentDate
            }else{
                LimitDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
                textView.text = LimitDate
            }
            Log.i(TAG, "showDatePicker: ${textView.text}")
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun showTimePicker(textView: TextView) {

        val timePicker = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                textView.text = selectedTime
                currentTime = selectedTime
                selectTime = true
                Log.i(TAG, "showTimePicker: $selectedTime")
            },
            hour,
            minute,
            false
        )
        timePicker.show()
    }

    fun onRbClicked(view:View){
        when(view.id){
            _binding.radioButtonAlarm.id ->{
                isAlarm = true
            }
            _binding.radioButtonNotification.id ->{
                isAlarm = false
            }
        }
    }

    fun onBtnClicked(view:View){
        if(isAlarm == true && selectTime){
            //save item and send it to viewmodel
            //insert in room
            //getList then upgrade ui
            //work alarm or notification
        }
    }

}