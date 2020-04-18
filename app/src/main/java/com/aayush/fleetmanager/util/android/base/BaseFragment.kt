package com.aayush.fleetmanager.util.android.base

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB: ViewBinding>: Fragment() {
    protected var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}