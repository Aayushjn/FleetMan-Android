package com.aayush.fleetmanager.util.android.base

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Abstract base class for fragments inflated using ViewBinding
 *
 * [VB] is the main view binding of the fragment
 * e.g., for DashboardFragment it would be FragmentDashboardViewBinding
 *
 * [MVB] is the merge view binding of the fragment used for layouts that include a layout with a
 * parent <merge> tag
 */
abstract class BaseFragment<VB: ViewBinding, MVB: ViewBinding> : Fragment() {
    protected var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    protected var _mergeBinding: MVB? = null
    protected val mergeBinding: MVB
        get() = _mergeBinding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _mergeBinding = null
        _binding = null
    }
}