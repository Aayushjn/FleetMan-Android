package com.aayush.fleetmanager.util.android.base

import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<VB: ViewBinding>: BottomSheetDialogFragment() {
    protected var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}