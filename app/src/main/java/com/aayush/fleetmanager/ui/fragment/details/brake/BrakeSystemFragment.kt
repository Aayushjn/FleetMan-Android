package com.aayush.fleetmanager.ui.fragment.details.brake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.databinding.FragmentBrakeSystemBinding
import com.aayush.fleetmanager.util.android.base.BaseBottomSheetDialogFragment
import com.aayush.fleetmanager.util.common.EXTRA_BRAKE_FLUID_LEVEL
import com.aayush.fleetmanager.util.common.EXTRA_BRAKE_TEMP
import com.aayush.fleetmanager.util.common.EXTRA_BRAKE_TYPE
import com.aayush.fleetmanager.util.common.format
import kotlinx.android.synthetic.main.fragment_brake_system.*

class BrakeSystemFragment: BaseBottomSheetDialogFragment<FragmentBrakeSystemBinding>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBrakeSystemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: Bundle = requireArguments()

        text_brake_fluid_level.text = requireContext().getString(
            R.string.set_health,
            "Brake Fluid Level",
            (args[EXTRA_BRAKE_FLUID_LEVEL] as Double).format("%")
        )
        text_brake_temp.text = requireContext().getString(
            R.string.set_health,
            "Brake Temperature",
            (args[EXTRA_BRAKE_TEMP] as Double).format("\u00b0C")
        )
        text_brake_type.text = requireContext().getString(
            R.string.set_health,
            "Brake Type",
            args[EXTRA_BRAKE_TYPE].toString()
        )
    }
}