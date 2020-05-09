package com.aayush.fleetmanager.ui.fragment.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.databinding.CardAlertBinding
import com.aayush.fleetmanager.model.Alert
import com.aayush.fleetmanager.util.common.EXTRA_ALERT_ID
import com.aayush.fleetmanager.util.common.EXTRA_EMAIL
import com.aayush.fleetmanager.util.common.EXTRA_ROLE
import com.aayush.fleetmanager.util.common.EXTRA_VIN

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
            )
        ).apply {
            setOnClickListener {
                val alert = alerts[adapterPosition]
                clickAction()
                navController.navigate(
                    R.id.navigate_to_vehicle_details_fragment,
                    bundleOf(
                        EXTRA_ALERT_ID to alert.id,
                        EXTRA_VIN to alert.vin,
                        EXTRA_EMAIL to email,
                        EXTRA_ROLE to role
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: AlertsViewHolder, position: Int) = holder.bindTo(alerts[position])

    override fun getItemCount(): Int = alerts.size

    fun submitList(newList: List<Alert>) {
        val start = alerts.size
        alerts.addAll(newList)
        notifyItemRangeInserted(start, newList.size)
    }

    fun clear() {
        val size = alerts.size
        alerts.clear()
        notifyItemRangeRemoved(0, size)
    }
}