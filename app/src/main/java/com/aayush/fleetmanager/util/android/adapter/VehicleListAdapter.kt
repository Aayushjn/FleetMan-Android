package com.aayush.fleetmanager.util.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.model.VehicleMinimal
import com.google.android.material.textview.MaterialTextView

class VehicleListAdapter(
    private val context: Context,
    private val titles: List<String>,
    private val details: HashMap<String, List<VehicleMinimal>>
): BaseExpandableListAdapter() {
    override fun getGroupCount(): Int = titles.size

    override fun getChildrenCount(groupPosition: Int): Int = details[titles[groupPosition]]!!.size

    override fun getGroup(groupPosition: Int): Any = titles[groupPosition]

    override fun getChild(groupPosition: Int, childPosition: Int): Any = details[titles[groupPosition]]!![childPosition]

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun hasStableIds(): Boolean = false

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    @SuppressLint("InflateParams")
    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_group_item, null)
        view.findViewById<MaterialTextView>(R.id.text_vehicle_title).text = titles[groupPosition]
        return view
    }

    @SuppressLint("InflateParams")
    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_child_item, null)
        with(details[titles[groupPosition]]!![childPosition]) {
            view.findViewById<MaterialTextView>(R.id.text_vehicle_vin).text = context.getString(R.string.set_vin, vin)
            view.findViewById<MaterialTextView>(R.id.text_vehicle_license_plate).text = context.getString(R.string.set_license_plate, licensePlate)
            view.findViewById<MaterialTextView>(R.id.text_vehicle_model).text = model
        }
        return view
    }
}