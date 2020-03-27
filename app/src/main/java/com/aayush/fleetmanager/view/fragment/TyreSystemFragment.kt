package com.aayush.fleetmanager.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.util.common.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.tyre_system_layout.*

class TyreSystemFragment: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.tyre_system_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: Bundle = requireArguments()

        text_tyre_pressure.text = context?.getString(
            R.string.set_health,
            "Tyre Pressure",
            (args[EXTRA_TYRE_PRESSURE] as Double).format(3, " psi")
        )
        text_tyre_rating.text = context?.getString(
            R.string.set_health,
            "Tyre Rating",
            args[EXTRA_TYRE_RATING].toString()
        )
        text_tyre_temp.text = context?.getString(
            R.string.set_health,
            "Tyre Temp",
            (args[EXTRA_TYRE_TEMP] as Double).format(3, "\u00b0C")
        )
        text_tyre_wear.text = context?.getString(
            R.string.set_health,
            "Tyre Wear",
            (args[EXTRA_TYRE_WEAR] as Double).format(3, " mm")
        )
    }

    companion object {
        @JvmStatic fun newInstance(
            tyrePressure: Double?,
            tyreRating: String?,
            tyreTemp: Double?,
            tyreWear: Double?
        ): TyreSystemFragment = TyreSystemFragment().apply {
            val args = Bundle().apply {
                putDouble(EXTRA_TYRE_PRESSURE, tyrePressure ?: 0.0)
                putString(EXTRA_TYRE_RATING, tyreRating ?: "")
                putDouble(EXTRA_TYRE_TEMP, tyreTemp ?: 0.0)
                putDouble(EXTRA_TYRE_WEAR, tyreWear ?: 0.0)
            }
            arguments = args
        }
    }
}