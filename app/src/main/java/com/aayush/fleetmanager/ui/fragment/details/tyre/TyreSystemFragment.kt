package com.aayush.fleetmanager.ui.fragment.details.tyre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.databinding.FragmentTyreSystemBinding
import com.aayush.fleetmanager.util.android.base.BaseBottomSheetDialogFragment
import com.aayush.fleetmanager.util.common.*
import kotlinx.android.synthetic.main.fragment_tyre_system.*

class TyreSystemFragment: BaseBottomSheetDialogFragment<FragmentTyreSystemBinding>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentTyreSystemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: Bundle = requireArguments()

        text_tyre_pressure.text = requireContext().getString(
            R.string.set_health,
            "Tyre Pressure",
            (args[EXTRA_TYRE_PRESSURE] as Double).format(" psi")
        )
        text_tyre_rating.text = requireContext().getString(
            R.string.set_health,
            "Tyre Rating",
            args[EXTRA_TYRE_RATING].toString()
        )
        text_tyre_temp.text = requireContext().getString(
            R.string.set_health,
            "Tyre Temp",
            (args[EXTRA_TYRE_TEMP] as Double).format("\u00b0C")
        )
        text_tyre_wear.text = requireContext().getString(
            R.string.set_health,
            "Tyre Wear",
            (args[EXTRA_TYRE_WEAR] as Double).format(" mm")
        )
    }
}