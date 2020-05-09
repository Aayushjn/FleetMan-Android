package com.aayush.fleetmanager.ui.fragment.details.engine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.databinding.FragmentEngineSystemBinding
import com.aayush.fleetmanager.util.android.base.BaseBottomSheetDialogFragment
import com.aayush.fleetmanager.util.common.EXTRA_ENGINE_BATTERY_CAPACITY
import com.aayush.fleetmanager.util.common.EXTRA_ENGINE_TEMP
import com.aayush.fleetmanager.util.common.format
import kotlinx.android.synthetic.main.fragment_engine_system.*

class EngineSystemFragment: BaseBottomSheetDialogFragment<FragmentEngineSystemBinding>() {    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEngineSystemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val args: Bundle = requireArguments()

        text_engine_battery_capacity.text = requireContext().getString(
            R.string.set_health,
            "Engine Battery Capacity",
            (args[EXTRA_ENGINE_BATTERY_CAPACITY] as Double).format("%")
        )
        text_engine_temp.text = requireContext().getString(
            R.string.set_health,
            "Engine Temperature",
            (args[EXTRA_ENGINE_TEMP] as Double).format("\u00b0C")
        )
    }
}