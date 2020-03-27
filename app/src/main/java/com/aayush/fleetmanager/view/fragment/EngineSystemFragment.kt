package com.aayush.fleetmanager.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.util.common.EXTRA_ENGINE_BATTERY_CAPACITY
import com.aayush.fleetmanager.util.common.EXTRA_ENGINE_TEMP
import com.aayush.fleetmanager.util.common.format
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.engine_system_layout.*

class EngineSystemFragment: BottomSheetDialogFragment() {    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.engine_system_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val args: Bundle = requireArguments()

        text_engine_battery_capacity.text = context?.getString(
            R.string.set_health,
            "Engine Battery Capacity",
            (args[EXTRA_ENGINE_BATTERY_CAPACITY] as Double).format(3, "%")
        )
        text_engine_temp.text = context?.getString(
            R.string.set_health,
            "Engine Temperature",
            (args[EXTRA_ENGINE_TEMP] as Double).format(3, "\u00b0C")
        )
    }

    companion object {
        @JvmStatic fun newInstance(
            engineBatteryCapacity: Double?,
            engineTemp: Double?
        ): EngineSystemFragment = EngineSystemFragment().apply {
            val args = Bundle().apply {
                putDouble(EXTRA_ENGINE_BATTERY_CAPACITY, engineBatteryCapacity ?: 0.0)
                putDouble(EXTRA_ENGINE_TEMP, engineTemp ?: 0.0)
            }
            arguments = args
        }
    }
}