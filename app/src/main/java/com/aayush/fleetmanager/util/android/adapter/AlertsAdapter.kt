package com.aayush.fleetmanager.util.android.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.model.Alert
import com.aayush.fleetmanager.model.AlertType
import com.aayush.fleetmanager.util.common.EXTRA_ALERT_ID
import com.aayush.fleetmanager.util.common.EXTRA_EMAIL
import com.aayush.fleetmanager.util.common.EXTRA_ROLE
import com.aayush.fleetmanager.util.common.EXTRA_VIN
import com.aayush.fleetmanager.view.activity.VehicleDetailsActivity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_alert.view.*

class AlertsAdapter(
    private val context: Context,
    private val email: String,
    private val role: String,
    private val alerts: List<Alert>
): RecyclerView.Adapter<AlertsAdapter.AlertsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertsViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.card_alert, parent, false)
        return AlertsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertsViewHolder, position: Int) = holder.bind(alerts[position])

    override fun getItemCount(): Int = alerts.size

    inner class AlertsViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(alert: Alert) = with(containerView) {
            card_alert.setOnClickListener {
                val intent = Intent(context, VehicleDetailsActivity::class.java).apply {
                    putExtra(EXTRA_ALERT_ID, alert.id)
                    putExtra(EXTRA_VIN, alert.vin)
                    putExtra(EXTRA_EMAIL, email)
                    putExtra(EXTRA_ROLE, role)
                }
                context.startActivity(intent)
            }
            text_vin.text = context.getString(R.string.set_vin, alert.vin)
            text_license_plate.text = context.getString(R.string.set_license_plate, alert.licensePlate)
            text_alert.text = alert.text
            when(alert.type) {
                AlertType.SEVERE -> {
                    card_alert.setBackgroundColor(ContextCompat.getColor(context, R.color.severe))
                    text_vin.setTextColor(ContextCompat.getColor(context, R.color.colorOnError))
                    text_license_plate.setTextColor(ContextCompat.getColor(context, R.color.colorOnError))
                    text_alert.setTextColor(ContextCompat.getColor(context, R.color.colorOnError))
                }
                AlertType.WARNING -> {
                    card_alert.setBackgroundColor(ContextCompat.getColor(context, R.color.warning))
                    text_vin.setTextColor(ContextCompat.getColor(context, R.color.colorOnSecondary))
                    text_license_plate.setTextColor(ContextCompat.getColor(context, R.color.colorOnSecondary))
                    text_alert.setTextColor(ContextCompat.getColor(context, R.color.colorOnSecondary))
                }
            }
        }
    }
}