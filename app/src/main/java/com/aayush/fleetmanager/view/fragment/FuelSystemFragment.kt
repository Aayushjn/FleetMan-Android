package com.aayush.fleetmanager.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.util.common.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fuel_system_layout.*

class FuelSystemFragment: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fuel_system_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: Bundle = requireArguments()

        text_fuel_capacity.text = context?.getString(
            R.string.set_health,
            "Fuel Capacity",
            (args[EXTRA_FUEL_CAPACITY] as Double).format(3, " L")
        )
        text_fuel_level.text = context?.getString(
            R.string.set_health,
            "Fuel Level",
            (args[EXTRA_FUEL_LEVEL] as Double).format(3, "%")
        )
        text_fuel_rating.text = context?.getString(
            R.string.set_health,
            "Fuel Rating",
            args[EXTRA_FUEL_RATING].toString()
        )
        text_fuel_type.text = context?.getString(
            R.string.set_health,
            "Fuel Type",
            args[EXTRA_FUEL_TYPE].toString()
        )
    }

    companion object {
        @JvmStatic fun newInstance(
            fuelCapacity: Double?,
            fuelLevel: Double?,
            fuelRating: String?,
            fuelType: String?
        ): FuelSystemFragment = FuelSystemFragment().apply {
            val args = Bundle().apply {
                putDouble(EXTRA_FUEL_CAPACITY, fuelCapacity ?: 0.0)
                putDouble(EXTRA_FUEL_LEVEL, fuelLevel ?: 0.0)
                putString(EXTRA_FUEL_RATING, fuelRating ?: "")
                putString(EXTRA_FUEL_TYPE, fuelType ?: "")
            }
            arguments = args
        }
    }
}