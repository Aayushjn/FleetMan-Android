package com.aayush.fleetmanager.util.android

import android.content.Context
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {
    private var parentContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentContext = context
    }

    override fun onDetach() {
        super.onDetach()
        parentContext = null
    }

    protected fun requireParentContext(): Context {
        return parentContext ?: throw IllegalStateException("Fragment $this not attached to a parent context.")
    }
}