package com.aayush.fleetmanager.util.android.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.aayush.fleetmanager.util.android.showIf

class EmptySupportRecyclerView: RecyclerView {
    var emptyView: View? = null
        set(value) {
            field = value
            checkIfEmpty()
        }
    private val observer = object: AdapterDataObserver() {
        override fun onChanged() = checkIfEmpty()
        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = checkIfEmpty()
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = checkIfEmpty()
    }

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    override fun setAdapter(adapter: Adapter<*>?) {
        this.adapter?.unregisterAdapterDataObserver(observer)
        super.setAdapter(adapter)
        this.adapter?.registerAdapterDataObserver(observer)
        checkIfEmpty()
    }

    private fun checkIfEmpty() {
        if (emptyView != null && adapter != null) {
            val isEmptyViewVisible = adapter!!.itemCount == 0
            emptyView!!.showIf(isEmptyViewVisible)
            showIf(!isEmptyViewVisible)
        }
    }
}