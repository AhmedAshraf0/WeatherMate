package com.example.weathermate.dialog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.weathermate.R
import com.example.weathermate.databinding.FragmentDialogBinding

class MyDialogFragment : DialogFragment() {
    lateinit var _binding : FragmentDialogBinding
    var okClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentDialogBinding.inflate(inflater)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.okButton.setOnClickListener {
            Log.i("TAG", "onViewCreated: ok")
            getTargetFragment()?.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, Intent())
            dismiss()
        }

        _binding.cancelButton.setOnClickListener {
            Log.i("TAG", "onViewCreated: cancel")
            activity?.finishAffinity()
        }
    }

    fun cancel(view : View){

    }

    fun navToSettings(view : View){

    }
}