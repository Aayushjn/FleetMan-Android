package com.aayush.fleetmanager.ui.fragment.dashboard

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.databinding.CardAlertBinding
import com.aayush.fleetmanager.model.Alert
import com.aayush.fleetmanager.model.AlertType

class AlertsViewHolder(
    private val binding: CardAlertBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bindTo(alert: Alert) = with(binding) {
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

    fun setOnClickListener(listener: ((View) -> Unit)?) = binding.root.setOnClickListener(listener)
}