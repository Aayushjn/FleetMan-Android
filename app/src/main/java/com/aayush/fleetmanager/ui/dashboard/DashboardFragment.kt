package com.aayush.fleetmanager.ui.dashboard

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aayush.fleetmanager.App
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.api.RestApi
import com.aayush.fleetmanager.databinding.DialogAlertsBinding
import com.aayush.fleetmanager.databinding.DialogInputBinding
import com.aayush.fleetmanager.databinding.FragmentDashboardBinding
import com.aayush.fleetmanager.db.dao.AlertDao
import com.aayush.fleetmanager.di.component.DaggerFragmentComponent
import com.aayush.fleetmanager.di.component.FragmentComponent
import com.aayush.fleetmanager.di.module.AppModule
import com.aayush.fleetmanager.di.module.RoomModule
import com.aayush.fleetmanager.model.VehicleCount
import com.aayush.fleetmanager.util.android.*
import com.aayush.fleetmanager.util.android.adapter.VehicleListAdapter
import com.aayush.fleetmanager.util.android.base.BaseFragment
import com.aayush.fleetmanager.util.common.EXTRA_EMAIL
import com.aayush.fleetmanager.util.common.EXTRA_ROLE
import com.aayush.fleetmanager.util.common.EXTRA_VIN
import com.aayush.fleetmanager.util.common.State.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DashboardFragment: BaseFragment<FragmentDashboardBinding>() {
    private val component: FragmentComponent by lazy(LazyThreadSafetyMode.NONE) {
        DaggerFragmentComponent.builder()
            .appModule(AppModule(requireContext().applicationContext as App))
            .roomModule(RoomModule(requireContext().applicationContext as App))
            .build()
    }
    @Inject lateinit var restApi: RestApi
    @Inject lateinit var sharedPreferences: SharedPreferences
    @Inject lateinit var alertDao: AlertDao

    private lateinit var email: String
    private lateinit var role: String

    private lateinit var alertCountTextView: TextView

    private val dashboardViewModel: DashboardViewModel by viewModels { DashboardViewModelFactory(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        component.inject(this)

        if (!isUserLoggedIn(sharedPreferences)) {
            findNavController().navigate(DashboardFragmentDirections.navigateToLoginFragment())
        } else {
            val pair = getLoggedInUserEmailAndRole(sharedPreferences)
            email = pair.first
            role = pair.second
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadViews()
        setupListeners()
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    Timber.w(it.exception, "getInstanceId() failed")
                    return@addOnCompleteListener
                }
                val token = it.result?.token
                runBlocking(Dispatchers.IO) {
                    if (::email.isInitialized) {
                        restApi.registerToken(email, token ?: "")
                    }
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)

        val menuItem: MenuItem = menu.findItem(R.id.alerts)
        alertCountTextView = menuItem.actionView.findViewById(R.id.text_badge)
        setupBadge()
        menuItem.actionView.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressWarnings("InflateParams")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                dashboardViewModel.getLogoutResult(email).observe(viewLifecycleOwner) {
                    when(it) {
                        is Success<*> -> {
                            binding.layoutProgress.progressBar.visibility = View.GONE
                            logoutUser(sharedPreferences)
                            findNavController().navigate(DashboardFragmentDirections.navigateToLoginFragment())
                        }
                        is Failure -> {
                            binding.layoutProgress.progressBar.visibility = View.GONE
                            requireContext().toast(it.reason)
                        }
                        is Loading -> binding.layoutProgress.progressBar.visibility = View.VISIBLE
                    }
                }
                true
            }
            R.id.search -> {
                val dialogBinding = DialogInputBinding.inflate(LayoutInflater.from(requireContext()))
                dialogBinding.editVin.hint = requireContext().getString(R.string.vin)
                dialogBinding.groupChoices.setOnCheckedChangeListener { group, checkedId ->
                    dialogBinding.editVin.hint = group.findViewById<MaterialRadioButton>(checkedId).text
                }
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Search individual vehicles")
                    .setView(dialogBinding.root)
                    .setPositiveButton("Search") { dialog, _ ->
                        dialog.dismiss()
                        if (!dialogBinding.editVin.isBlankOrEmpty()) {
                            findNavController().navigateSafe(
                                R.id.navigation_dashboard,
                                R.id.navigate_to_vehicle_details_fragment,
                                bundleOf(
                                    EXTRA_EMAIL to email,
                                    EXTRA_ROLE to role,
                                    EXTRA_VIN to dialogBinding.editVin.asString()
                                )
                            )
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    .show()
                true
            }
            R.id.alerts -> {
                val dialogBinding = DialogAlertsBinding.inflate(LayoutInflater.from(requireContext()))

                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Alerts")
                    .setView(dialogBinding.root)
                    .show()

                dialogBinding.recyclerAlerts.apply {
                    layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    setHasFixedSize(true)

                    dashboardViewModel.alertResult.observe(viewLifecycleOwner) {
                        if (adapter == null) {
                            adapter = AlertsAdapter(
                                findNavController(),
                                email,
                                role,
                                it.toMutableList()
                            ) { dialog.dismiss() }
                        } else {
                            (adapter as AlertsAdapter).submitList(it)
                        }
                        if (it.isNotEmpty()) {
                            dialogBinding.btnClear.visibility = View.VISIBLE
                        }
                    }
                }

                dialogBinding.btnClear.setOnClickListener {
                    if (dialogBinding.recyclerAlerts.adapter == null) {
                        dialogBinding.recyclerAlerts.adapter = AlertsAdapter(
                            findNavController(),
                            email,
                            role,
                            mutableListOf()
                        ) { dialog.dismiss() }
                    } else {
                        (dialogBinding.recyclerAlerts.adapter as AlertsAdapter).clear()
                    }
                    lifecycleScope.launch(Dispatchers.IO) { alertDao.deleteAll() }
                    it.visibility = View.GONE
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadViews() {
        dashboardViewModel.vehicleCountResult.observe(viewLifecycleOwner) {
            when(it) {
                is Loading -> binding.veilCount.veil()
                is Success<*> -> {
                    binding.veilCount.unVeil()
                    with(it.data!! as VehicleCount) {
                        val totalCount: Int = hatchbacks + sedans + suvs
                        with(binding) {
                            textVehicleCount.text = totalCount.toString()
                            textHatchbackCount.text = requireContext().getString(
                                R.string.vehicle_count,
                                hatchbacks,
                                requireContext().resources.getQuantityString(R.plurals.hatchback, hatchbacks)
                            )
                            textSedanCount.text = requireContext().getString(
                                R.string.vehicle_count,
                                sedans,
                                requireContext().resources.getQuantityString(R.plurals.sedan, sedans)
                            )
                            textSuvCount.text = requireContext().getString(
                                R.string.vehicle_count,
                                suvs,
                                requireContext().resources.getQuantityString(R.plurals.suv, suvs)
                            )
                            textVehicles.text = requireContext().resources.getQuantityString(R.plurals.vehicle, totalCount)
                        }
                    }
                }
                is Failure -> {
                    binding.veilVehicles.veil()
                    requireContext().toast(it.reason)
                }
            }
        }

        binding.recyclerVehicles.apply {
            dashboardViewModel.vehicleMinimalsResult.observe(viewLifecycleOwner) {
                when(it) {
                    is Loading -> binding.veilVehicles.veil()
                    is Success<*> -> {
                        binding.veilVehicles.unVeil()
                        val minimalReturn = it.data!! as MinimalReturn
                        setAdapter(
                            VehicleListAdapter(
                                requireContext(),
                                minimalReturn.first,
                                minimalReturn.second
                            )
                        )
                    }
                    is Failure -> {
                        binding.veilVehicles.veil()
                        requireContext().toast(it.reason)
                    }
                }
            }
            if (expandableListAdapter is VehicleListAdapter) {
                setViewHeight(-1)
            }
        }
    }

    private fun setupListeners() {
        binding.recyclerVehicles.apply {
            setOnGroupClickListener { parent, _, groupPosition, _ ->
                parent.setViewHeight(groupPosition)
                false
            }

            setOnChildClickListener { _, v, _, _, _ ->
                val vin: String = v.findViewById<MaterialTextView>(R.id.text_vehicle_vin).text
                    .toString()
                    .substring(5)
                    .trim()
                findNavController().navigateSafe(
                    R.id.navigation_dashboard,
                    R.id.navigate_to_vehicle_details_fragment,
                    bundleOf(
                        EXTRA_EMAIL to email,
                        EXTRA_ROLE to role,
                        EXTRA_VIN to vin
                    )
                )
                true
            }
        }
    }

    private fun setupBadge() {
        dashboardViewModel.alertCountResult.observe(viewLifecycleOwner) {
            alertCountTextView.text = it.toString()
            alertCountTextView.showIf(it != 0)
        }
    }
}
