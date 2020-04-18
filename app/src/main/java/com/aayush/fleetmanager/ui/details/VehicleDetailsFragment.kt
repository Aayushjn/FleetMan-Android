package com.aayush.fleetmanager.ui.details

import android.graphics.Typeface.BOLD
import android.os.Bundle
import android.os.ParcelUuid
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.aayush.fleetmanager.App
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.databinding.FragmentVehicleDetailsBinding
import com.aayush.fleetmanager.db.dao.AlertDao
import com.aayush.fleetmanager.di.component.DaggerFragmentComponent
import com.aayush.fleetmanager.di.component.FragmentComponent
import com.aayush.fleetmanager.di.module.AppModule
import com.aayush.fleetmanager.di.module.RoomModule
import com.aayush.fleetmanager.model.Role
import com.aayush.fleetmanager.model.Vehicle
import com.aayush.fleetmanager.util.android.base.BaseFragment
import com.aayush.fleetmanager.util.android.navigateSafe
import com.aayush.fleetmanager.util.android.toast
import com.aayush.fleetmanager.util.common.*
import com.aayush.fleetmanager.util.common.State.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class VehicleDetailsFragment: BaseFragment<FragmentVehicleDetailsBinding>() {
    private val component: FragmentComponent by lazy(LazyThreadSafetyMode.NONE) {
        DaggerFragmentComponent.builder()
            .appModule(AppModule(requireContext().applicationContext as App))
            .roomModule(RoomModule(requireContext().applicationContext as App))
            .build()
    }
    @Inject lateinit var alertDao: AlertDao

    private val vin: String by lazy(LazyThreadSafetyMode.NONE) { requireArguments()[EXTRA_VIN] as String }
    private val email: String by lazy(LazyThreadSafetyMode.NONE) { requireArguments()[EXTRA_EMAIL] as String }
    private val role: String by lazy(LazyThreadSafetyMode.NONE) { requireArguments()[EXTRA_ROLE] as String }

    private lateinit var vehicle: Vehicle
    private val vehicleDetailsViewModel: VehicleDetailsViewModel by viewModels { VehicleDetailsViewModelFactory(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVehicleDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id: ParcelUuid? = requireArguments()[EXTRA_ALERT_ID] as? ParcelUuid
        if (id != null) {
            lifecycleScope.launch(Dispatchers.IO) { alertDao.deleteAlertById(id) }
        }

        observeData()
        setupListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.menu_details, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.refresh -> {
            binding.swipeRefresh.isRefreshing = true
            observeData()
            true
        }
        android.R.id.home -> {
            findNavController().navigateUp()
            true
        }
        else -> super.onOptionsItemSelected(item)
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
        binding.textVehicleVin.text = SpannableString(requireContext().getString(
            R.string.set_vin,
            vin
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        binding.textVehicleLicensePlate.text = SpannableString(requireContext().getString(
            R.string.set_license_plate,
            licensePlate
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        binding.textVehicleModel.text = SpannableString(requireContext().getString(
            R.string.set_health,
            "Model",
            model
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        binding.textVehicleDriver.text = SpannableString(requireContext().getString(
            R.string.set_health,
            "Driver",
            driver
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        binding.textVehicleBrakeHealth.text = SpannableString(requireContext().getString(
            R.string.set_health,
            "Braking System Health",
            brakes?.health?.format() ?: 0.0
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 21, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        binding.textVehicleTyreHealth.text = SpannableString(requireContext().getString(
            R.string.set_health,
            "Tyre System Health",
            tyres?.health?.format() ?: 0.0
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 18, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        binding.textVehicleEngineHealth.text = SpannableString(requireContext().getString(
            R.string.set_health,
            "Engine System Health",
            engine?.health?.format() ?: 0.0
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 20, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        binding.textVehicleFuelHealth.text = SpannableString(requireContext().getString(
            R.string.set_health,
            "Fuel System Health",
            fuel?.health?.format() ?: 0.0
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 18, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        binding.textVehicleHealth.text = SpannableString(requireContext().getString(
            R.string.set_health,
            "Overall Health",
            overallHealth?.format() ?: 0.0
        )).apply {
            setSpan(StyleSpan(BOLD), 0, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }

    private fun loadMaintenanceView() = with(vehicle) {
        binding.textVehicleVin.visibility = View.GONE
        binding.textVehicleLicensePlate.visibility = View.GONE
        binding.textVehicleDriver.visibility = View.GONE

        binding.textVehicleModel.text = requireContext().getString(R.string.set_health, "Model", model)
        binding.textVehicleBrakeHealth.text = requireContext().getString(
            R.string.set_health,
            "Braking System Health",
            brakes?.health?.format() ?: 0.0
        )
        binding.textVehicleTyreHealth.text = requireContext().getString(
            R.string.set_health,
            "Tyre System Health",
            tyres?.health?.format() ?: 0.0
        )
        binding.textVehicleEngineHealth.text = requireContext().getString(
            R.string.set_health,
            "Engine System Health",
            engine?.health?.format() ?: 0.0
        )
        binding.textVehicleFuelHealth.text = requireContext().getString(
            R.string.set_health,
            "Fuel System Health",
            fuel?.health?.format() ?: 0.0
        )
        binding.textVehicleHealth.text = requireContext().getString(
            R.string.set_health,
            "Overall Health",
            overallHealth?.format() ?: 0.0
        )
    }

    private fun loadInsuranceView() = with(vehicle) {
        binding.textVehicleVin.text = requireContext().getString(R.string.set_vin, vin)
        binding.textVehicleLicensePlate.text = requireContext().getString(R.string.set_license_plate, licensePlate)
        binding.textVehicleModel.text = requireContext().getString(R.string.set_health, "Model", model)

        binding.textVehicleDriver.visibility = View.GONE
        binding.textVehicleBrakeHealth.visibility = View.GONE
        binding.textVehicleTyreHealth.visibility = View.GONE
        binding.textVehicleEngineHealth.visibility = View.GONE
        binding.textVehicleFuelHealth.visibility = View.GONE
        binding.textVehicleHealth.visibility = View.GONE
        binding.cardBrakeSystem.visibility = View.GONE
        binding.cardEngineSystem.visibility = View.GONE
        binding.cardFuelSystem.visibility = View.GONE
        binding.cardTyreSystem.visibility = View.GONE
    }

    private fun loadDevView() {
        binding.cardBaseDetails.visibility = View.GONE
        binding.cardBrakeSystem.visibility = View.GONE
        binding.cardEngineSystem.visibility = View.GONE
        binding.cardFuelSystem.visibility = View.GONE
        binding.cardTyreSystem.visibility = View.GONE

        Snackbar.make(binding.root, "No data for back-end devs", Snackbar.LENGTH_SHORT).show()
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener { observeData() }

        binding.cardBrakeSystem.setOnClickListener {
            with(vehicle.brakes!!) {
                findNavController().navigateSafe(
                    R.id.navigation_vehicle_details,
                    R.id.navigate_to_brake_system_fragment,
                    bundleOf(
                        EXTRA_BRAKE_FLUID_LEVEL to fluidLevel,
                        EXTRA_BRAKE_TEMP to temp,
                        EXTRA_BRAKE_TYPE to type
                    )
                )
            }
        }

        binding.cardEngineSystem.setOnClickListener {
            with(vehicle.engine!!) {
                findNavController().navigateSafe(
                    R.id.navigation_vehicle_details,
                    R.id.navigate_to_engine_system_fragment,
                    bundleOf(
                        EXTRA_ENGINE_BATTERY_CAPACITY to batteryCapacity,
                        EXTRA_ENGINE_TEMP to temp
                    )
                )
            }
        }

        binding.cardFuelSystem.setOnClickListener {
            with(vehicle.fuel!!) {
                findNavController().navigateSafe(
                    R.id.navigation_vehicle_details,
                    R.id.navigate_to_fuel_system_fragment,
                    bundleOf(
                        EXTRA_FUEL_CAPACITY to capacity,
                        EXTRA_FUEL_LEVEL to level,
                        EXTRA_BRAKE_TYPE to type,
                        EXTRA_FUEL_RATING to rating
                    )
                )
            }
        }

        binding.cardTyreSystem.setOnClickListener {
            with(vehicle.tyres!!) {
                findNavController().navigateSafe(
                    R.id.navigation_vehicle_details,
                    R.id.navigate_to_tyre_system_fragment,
                    bundleOf(
                        EXTRA_TYRE_PRESSURE to pressure,
                        EXTRA_TYRE_RATING to rating,
                        EXTRA_TYRE_TEMP to temp,
                        EXTRA_TYRE_WEAR to wear
                    )
                )
            }
        }
    }

    private fun observeData() {
        vehicleDetailsViewModel.getVehicleResult(vin, email).observe(viewLifecycleOwner) {
            when(it) {
                is Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                    binding.veilBaseDetails.veil()
                    binding.veilHealthDetails.veil()
                }
                is Success<*> -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.veilBaseDetails.unVeil()
                    binding.veilHealthDetails.unVeil()

                    vehicle = it.data as Vehicle
                    loadViews()
                }
                is Failure -> {
                    binding.veilBaseDetails.veil()
                    binding.veilHealthDetails.veil()
                    requireContext().toast(it.reason)
                    findNavController().navigateUp()
                }
            }
        }
    }
}