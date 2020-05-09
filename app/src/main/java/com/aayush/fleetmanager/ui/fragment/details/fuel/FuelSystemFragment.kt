package com.aayush.fleetmanager.ui.fragment.details.fuel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.databinding.FragmentFuelSystemBinding
import com.aayush.fleetmanager.util.android.base.BaseBottomSheetDialogFragment
import com.aayush.fleetmanager.util.common.*
import kotlinx.android.synthetic.main.fragment_fuel_system.*

class FuelSystemFragment: BaseBottomSheetDialogFragment<FragmentFuelSystemBinding>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFuelSystemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: Bundle = requireArguments()

        text_fuel_capacity.text = requireContext().getString(
            R.string.set_health,
            "Fuel Capacity",
            (args[EXTRA_FUEL_CAPACITY] as Double).format(" L")
        )
        text_fuel_level.text = requireContext().getString(
            R.string.set_health,
            "Fuel Level",
            (args[EXTRA_FUEL_LEVEL] as Double).format("%")
        )
        text_fuel_rating.text = requireContext().getString(
            R.string.set_health,
            "Fuel Rating",
            args[EXTRA_FUEL_RATING].toString()
        )
        text_fuel_type.text = requireContext().getString(
            R.string.set_health,
            "Fuel Type",
            args[EXTRA_FUEL_TYPE].toString()
        )
    }
}