package com.aayush.fleetmanager.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.util.common.EXTRA_BRAKE_FLUID_LEVEL
import com.aayush.fleetmanager.util.common.EXTRA_BRAKE_TEMP
import com.aayush.fleetmanager.util.common.EXTRA_BRAKE_TYPE
import com.aayush.fleetmanager.util.common.format
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.brake_system_layout.*

class BrakeSystemFragment: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.brake_system_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: Bundle = requireArguments()

        text_brake_fluid_level.text = context?.getString(
            R.string.set_health,
            "Brake Fluid Level",
            (args[EXTRA_BRAKE_FLUID_LEVEL] as Double).format(3, "%")
        )
        text_brake_temp.text = context?.getString(
            R.string.set_health,
            "Brake Temperature",
            (args[EXTRA_BRAKE_TEMP] as Double).format(3, "\u00b0C")
        )
        text_brake_type.text = context?.getString(
            R.string.set_health,
            "Brake Type",
            args[EXTRA_BRAKE_TYPE].toString()
        )
    }

    companion object {
        @JvmStatic fun newInstance(
            brakeFluidLevel: Double?,
            brakeTemp: Double?,
            brakeType: String?
        ): BrakeSystemFragment = BrakeSystemFragment().apply {
            val args = Bundle().apply {
                putDouble(EXTRA_BRAKE_FLUID_LEVEL, brakeFluidLevel ?: 0.0)
                putDouble(EXTRA_BRAKE_TEMP, brakeTemp ?: 0.0)
                putString(EXTRA_BRAKE_TYPE, brakeType ?: "")
            }
            arguments = args
        }
    }
}