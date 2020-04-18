package com.aayush.fleetmanager.ui.dashboard

import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.databinding.CardAlertBinding
import com.aayush.fleetmanager.model.Alert
import com.aayush.fleetmanager.model.AlertType
import com.aayush.fleetmanager.util.android.navigateSafe
import com.aayush.fleetmanager.util.common.EXTRA_ALERT_ID
import com.aayush.fleetmanager.util.common.EXTRA_EMAIL
import com.aayush.fleetmanager.util.common.EXTRA_ROLE
import com.aayush.fleetmanager.util.common.EXTRA_VIN

class AlertsViewHolder(
    private val binding: CardAlertBinding,
    private val navController: NavController,
    private val email: String,
    private val role: String,
    private val clickAction: () -> Unit
): RecyclerView.ViewHolder(binding.root) {
    fun bindTo(alert: Alert) = with(binding) {
        cardAlert.setOnClickListener {
            clickAction()
            navController.navigateSafe(
                R.id.navigation_dashboard,
                R.id.navigate_to_vehicle_details_fragment,
                bundleOf(
                    EXTRA_ALERT_ID to alert.id,
                    EXTRA_VIN to alert.vin,
                    EXTRA_EMAIL to email,
                    EXTRA_ROLE to role
                )
            )
        }
        binding.textVin.text = root.context.getString(R.string.set_vin, alert.vin)
        binding.textLicensePlate.text = root.context.getString(R.string.set_license_plate, alert.licensePlate)
        binding.textAlert.text = alert.text
        when(alert.type) {
            AlertType.SEVERE -> {
                binding.cardAlert.setBackgroundColor(ContextCompat.getColor(root.context, R.color.severe))
                binding.textVin.setTextColor(ContextCompat.getColor(root.context, android.R.color.white))
                binding.textLicensePlate.setTextColor(ContextCompat.getColor(root.context, android.R.color.white))
                binding.textAlert.setTextColor(ContextCompat.getColor(root.context, android.R.color.white))
            }
            AlertType.WARNING -> {
                binding.cardAlert.setBackgroundColor(ContextCompat.getColor(root.context, R.color.warning))
                binding.textVin.setTextColor(ContextCompat.getColor(root.context, android.R.color.black))
                binding.textLicensePlate.setTextColor(ContextCompat.getColor(root.context, android.R.color.black))
                binding.textAlert.setTextColor(ContextCompat.getColor(root.context, android.R.color.black))
            }
        }
    }
}