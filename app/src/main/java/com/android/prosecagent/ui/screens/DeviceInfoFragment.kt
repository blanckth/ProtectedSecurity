package com.android.prosecagent.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.prosecagent.R
import com.android.prosecagent.collectors.DeviceInfoCollector

class DeviceInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_device_info, container, false)
        val outputBox = view.findViewById<TextView>(R.id.deviceInfoBox)

        val metadata = DeviceInfoCollector.collect(requireContext())
        outputBox.text = metadata.toString().replace(",", "\n")

        return view
    }
}
