package com.aayush.fleetmanager.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.aayush.fleetmanager.databinding.CardAlertBinding
import com.aayush.fleetmanager.model.Alert

class AlertsAdapter(
    private val navController: NavController,
    private val email: String,
    private val role: String,
    private val alerts: MutableList<Alert>,
    private val clickAction: () -> Unit
): RecyclerView.Adapter<AlertsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertsViewHolder =
        AlertsViewHolder(
            CardAlertBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            navController,
            email,
            role,
            clickAction
        )

    override fun onBindViewHolder(holder: AlertsViewHolder, position: Int) = holder.bindTo(alerts[position])

    override fun getItemCount(): Int = alerts.size

    fun submitList(newList: List<Alert>) {
        val start = alerts.size + 1
        alerts.addAll(newList)
        notifyItemRangeInserted(start, newList.size)
    }

    fun clear() {
        val size = alerts.size
        alerts.clear()
        notifyItemRangeRemoved(0, size)
    }
}