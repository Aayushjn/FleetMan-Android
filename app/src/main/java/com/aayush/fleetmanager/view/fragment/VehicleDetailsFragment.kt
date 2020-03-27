package com.aayush.fleetmanager.view.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Typeface.BOLD
import android.os.Bundle
import android.os.ParcelUuid
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.*
import androidx.core.app.NavUtils
import androidx.lifecycle.observe
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.api.repository.VehicleRepository
import com.aayush.fleetmanager.model.Role
import com.aayush.fleetmanager.model.Vehicle
import com.aayush.fleetmanager.util.android.BaseFragment
import com.aayush.fleetmanager.util.android.getApp
import com.aayush.fleetmanager.util.android.toast
import com.aayush.fleetmanager.util.common.*
import com.aayush.fleetmanager.view.activity.MainActivity
import com.aayush.fleetmanager.view.activity.VehicleDetailsActivity
import com.aayush.fleetmanager.viewmodel.VehicleViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_vehicle_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VehicleDetailsFragment: BaseFragment() {
    private val vin: String by lazy(LazyThreadSafetyMode.NONE) { requireArguments()[EXTRA_VIN] as String }
    private val email: String by lazy(LazyThreadSafetyMode.NONE) { requireArguments()[EXTRA_EMAIL] as String }
    private val role: String by lazy(LazyThreadSafetyMode.NONE) { requireArguments()[EXTRA_ROLE] as String }

    private val pContext: Context by lazy(LazyThreadSafetyMode.NONE) { requireParentContext() }

    private lateinit var vehicle: Vehicle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_vehicle_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id: ParcelUuid? = requireArguments()[EXTRA_ALERT_ID] as? ParcelUuid
        if (id != null) {
            CoroutineScope(Dispatchers.IO).launch {
                pContext.getApp().database.alertDao().deleteAlertById(id)
            }
        }

        observeData()
        setupListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.refresh -> {
                swipe_refresh.isRefreshing = true
                observeData()
                true
            }
            android.R.id.home -> {
                val intent: Intent = NavUtils.getParentActivityIntent(pContext as VehicleDetailsActivity)!!.apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                NavUtils.navigateUpTo(pContext as VehicleDetailsActivity, intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadViews() {
        when(Role.valueOf(role)) {
            Role.ADMIN -> loadAdminView()
            Role.MAINTENANCE -> loadMaintenanceView()
            Role.INSURANCE -> loadInsuranceView()
            Role.DEV -> loadDevView()
        }
    }

    private fun loadAdminView() = with(vehicle) {
        text_vehicle_vin.text = SpannableString(pContext.getString(
            R.string.set_vin,
            vin
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        text_vehicle_license_plate.text = SpannableString(pContext.getString(
            R.string.set_license_plate,
            licensePlate
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        text_vehicle_model.text = SpannableString(pContext.getString(
            R.string.set_health,
            "Model",
            model
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        text_vehicle_driver.text = SpannableString(pContext.getString(
            R.string.set_health,
            "Driver",
            driver
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        text_vehicle_brake_health.text = SpannableString(pContext.getString(
            R.string.set_health,
            "Braking System Health",
            brakes?.health?.format(3) ?: 0.0
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 21, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        text_vehicle_tyre_health.text = SpannableString(pContext.getString(
            R.string.set_health,
            "Tyre System Health",
            tyres?.health?.format(3) ?: 0.0
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 18, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        text_vehicle_engine_health.text = SpannableString(pContext.getString(
            R.string.set_health,
            "Engine System Health",
            engine?.health?.format(3) ?: 0.0
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 20, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        text_vehicle_fuel_health.text = SpannableString(pContext.getString(
            R.string.set_health,
            "Fuel System Health",
            fuel?.health?.format(3) ?: 0.0
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 18, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        text_vehicle_health.text = SpannableString(pContext.getString(
            R.string.set_health,
            "Overall Health",
            overallHealth?.format(3) ?: 0.0
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }

    private fun loadMaintenanceView() = with(vehicle) {
        text_vehicle_vin.visibility = View.GONE
        text_vehicle_license_plate.visibility = View.GONE
        text_vehicle_driver.visibility = View.GONE

        text_vehicle_model.text = pContext.getString(R.string.set_health, "Model", model)
        text_vehicle_brake_health.text = pContext.getString(
            R.string.set_health,
            "Braking System Health",
            brakes?.health?.format(3) ?: 0.0
        )
        text_vehicle_tyre_health.text = pContext.getString(
            R.string.set_health,
            "Tyre System Health",
            tyres?.health?.format(3) ?: 0.0
        )
        text_vehicle_engine_health.text = pContext.getString(
            R.string.set_health,
            "Engine System Health",
            engine?.health?.format(3) ?: 0.0
        )
        text_vehicle_fuel_health.text = pContext.getString(
            R.string.set_health,
            "Fuel System Health",
            fuel?.health?.format(3) ?: 0.0
        )
        text_vehicle_health.text = pContext.getString(
            R.string.set_health,
            "Overall Health",
            overallHealth?.format(3) ?: 0.0
        )
    }

    private fun loadInsuranceView() = with(vehicle) {
        text_vehicle_vin.text = pContext.getString(R.string.set_vin, vin)
        text_vehicle_license_plate.text = pContext.getString(R.string.set_license_plate, licensePlate)
        text_vehicle_model.text = pContext.getString(R.string.set_health, "Model", model)

        text_vehicle_driver.visibility = View.GONE
        text_vehicle_brake_health.visibility = View.GONE
        text_vehicle_tyre_health.visibility = View.GONE
        text_vehicle_engine_health.visibility = View.GONE
        text_vehicle_fuel_health.visibility = View.GONE
        text_vehicle_health.visibility = View.GONE
        card_brake_system.visibility = View.GONE
        card_engine_system.visibility = View.GONE
        card_fuel_system.visibility = View.GONE
        card_tyre_system.visibility = View.GONE
    }

    private fun loadDevView() {
        card_base_details.visibility = View.GONE
        card_brake_system.visibility = View.GONE
        card_engine_system.visibility = View.GONE
        card_fuel_system.visibility = View.GONE
        card_tyre_system.visibility = View.GONE

        Snackbar.make(requireView(), "No data for back-end devs", Snackbar.LENGTH_SHORT).show()
    }

    private fun setupListeners() {
        swipe_refresh.setOnRefreshListener {
            observeData()
        }

        card_brake_system.setOnClickListener {
            with(vehicle.brakes!!) {
                val fragment: BrakeSystemFragment = BrakeSystemFragment.newInstance(
                    fluidLevel,
                    temp,
                    type
                )
                fragment.show(childFragmentManager, null)
            }
        }

        card_engine_system.setOnClickListener {
            with(vehicle.engine!!) {
                val fragment: EngineSystemFragment = EngineSystemFragment.newInstance(
                    batteryCapacity,
                    temp
                )
                fragment.show(childFragmentManager, null)
            }
        }

        card_fuel_system.setOnClickListener {
            with(vehicle.fuel!!) {
                val fragment: FuelSystemFragment = FuelSystemFragment.newInstance(
                    capacity,
                    level,
                    rating,
                    type
                )
                fragment.show(childFragmentManager, null)
            }
        }

        card_tyre_system.setOnClickListener {
            with(vehicle.tyres!!) {
                val fragment: TyreSystemFragment = TyreSystemFragment.newInstance(
                    pressure,
                    rating,
                    temp,
                    wear
                )
                fragment.show(childFragmentManager, null)
            }
        }
    }

    private fun observeData() {
        val vehicleViewModel = VehicleViewModel(
            VehicleRepository.getInstance(pContext.getApp().restApi),
            vin,
            email
        )

        vehicleViewModel.vehicleResult.observe(viewLifecycleOwner) {
            when(it) {
                is Loading -> {
                    swipe_refresh.isRefreshing = true
                    veil_base_details.veil()
                    veil_health_details.veil()
                }
                is Success<*> -> {
                    swipe_refresh.isRefreshing = false
                    veil_base_details.unVeil()
                    veil_health_details.unVeil()

                    vehicle = it.data as Vehicle
                    loadViews()
                }
                is Failure -> {
                    veil_base_details.veil()
                    veil_health_details.veil()
                    pContext.toast(it.message)
                    val intent = Intent(pContext, MainActivity::class.java).apply {
                        putExtra(EXTRA_EMAIL, email)
                        putExtra(EXTRA_ROLE, role)
                        putExtra(EXTRA_VIN, vin)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                    pContext.startActivity(intent)
                }
            }
        }
    }
}