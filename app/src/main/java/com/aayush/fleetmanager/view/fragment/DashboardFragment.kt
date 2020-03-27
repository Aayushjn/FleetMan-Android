package com.aayush.fleetmanager.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.api.repository.UserRepository
import com.aayush.fleetmanager.model.VehicleCount
import com.aayush.fleetmanager.util.android.*
import com.aayush.fleetmanager.util.android.adapter.AlertsAdapter
import com.aayush.fleetmanager.util.android.adapter.VehicleListAdapter
import com.aayush.fleetmanager.util.common.*
import com.aayush.fleetmanager.view.activity.MainActivity
import com.aayush.fleetmanager.view.activity.VehicleDetailsActivity
import com.aayush.fleetmanager.viewmodel.DashboardViewModel
import com.aayush.fleetmanager.viewmodel.DashboardViewModelFactory
import com.aayush.fleetmanager.viewmodel.LogoutViewModel
import com.aayush.fleetmanager.viewmodel.MinimalReturn
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.progress_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class DashboardFragment: BaseFragment() {
    private val email: String by lazy(LazyThreadSafetyMode.NONE) { requireArguments()[EXTRA_EMAIL] as String }
    private val role: String by lazy(LazyThreadSafetyMode.NONE) { requireArguments()[EXTRA_ROLE] as String }

    private val pContext: Context by lazy(LazyThreadSafetyMode.NONE) { requireParentContext() }

    private lateinit var alertCountTextView: TextView

    private val viewModel: DashboardViewModel by viewModels(factoryProducer = {
        DashboardViewModelFactory(pContext)
    })
    private lateinit var logoutViewModel: LogoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (pContext as MainActivity).supportActionBar?.title = "Dashboard"

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
                    pContext.getApp().restApi.registerToken(email, token ?: "")
                }
            }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)

        val menuItem: MenuItem = menu.findItem(R.id.alerts)
        alertCountTextView = menuItem.actionView.findViewById(R.id.text_badge)
        setupBadge()
        menuItem.actionView.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressWarnings("InflateParams")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                logoutViewModel = LogoutViewModel(
                    UserRepository.getInstance(pContext.getApp().restApi),
                    email
                )
                logoutViewModel.logoutResult.observe(viewLifecycleOwner) {
                    when(it) {
                        is Success<*> -> {
                            progress_wheel.visibility = View.GONE
                            logoutUser((pContext as MainActivity).getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE))
                            (pContext as MainActivity).supportFragmentManager.commit {
                                setCustomAnimations(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out,
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                                )
                                replace<LoginFragment>(R.id.fragment_container, TAG_LOGIN_FRAGMENT)
                            }
                        }
                        is Failure -> {
                            progress_wheel.visibility = View.GONE
                            pContext.toast(it.message)
                        }
                        is Loading -> progress_wheel.visibility = View.VISIBLE
                    }
                }
                true
            }
            R.id.search -> {
                val dialogLayout: View = LayoutInflater.from(pContext).inflate(R.layout.dialog_input, null)
                val editText: EditText? = dialogLayout.findViewById<EditText>(R.id.edit_vin)?.apply {
                    hint = getString(R.string.vin)
                }
                dialogLayout.findViewById<RadioGroup>(R.id.group_choices)?.setOnCheckedChangeListener { group, checkedId ->
                    editText?.hint = group.findViewById<MaterialRadioButton>(checkedId).text
                }
                MaterialAlertDialogBuilder(pContext)
                    .setTitle("Search individual vehicles")
                    .setView(dialogLayout)
                    .setPositiveButton("Search") { dialog, _ ->
                        if (editText?.isBlankOrEmpty() == false) {
                            val intent = Intent(pContext, VehicleDetailsActivity::class.java).apply {
                                putExtra(EXTRA_EMAIL, email)
                                putExtra(EXTRA_ROLE, role)
                                putExtra(EXTRA_VIN, editText.asString())
                            }
                            dialog.dismiss()
                            pContext.startActivity(intent)
                        } else {
                            dialog.dismiss()
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    .show()
                true
            }
            R.id.alerts -> {
                val dialogLayout: View = LayoutInflater.from(pContext).inflate(R.layout.dialog_alerts, null)
                val alertsRecycler: EmptyRecyclerView = dialogLayout.findViewById(R.id.recycler_alerts)
                val clearButton: MaterialButton = dialogLayout.findViewById(R.id.btn_clear)

                alertsRecycler.apply {
                    layoutManager = LinearLayoutManager(pContext, LinearLayoutManager.VERTICAL, false)
                    setHasFixedSize(true)
                    setEmptyView(dialogLayout.findViewById<TextView>(R.id.text_empty_state))

                    viewModel.alertResult.observe(viewLifecycleOwner) {
                        adapter = AlertsAdapter(pContext, email, role, it)
                        if (it.isNotEmpty()) {
                            clearButton.visibility = View.VISIBLE
                        }
                    }
                }

                clearButton.setOnClickListener {
                    alertsRecycler.adapter = AlertsAdapter(pContext, email, role, listOf())
                    clearButton.visibility = View.GONE
                }

                MaterialAlertDialogBuilder(pContext)
                    .setTitle("Alerts")
                    .setView(dialogLayout)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadViews() {
        viewModel.vehicleCountResult.observe(viewLifecycleOwner) {
            when(it) {
                is Loading -> veil_count.veil()
                is Success<*> -> {
                    veil_count.unVeil()
                    with(it.data!! as VehicleCount) {
                        val totalCount: Int = hatchbacks + sedans + suvs
                        text_vehicle_count.text = totalCount.toString()
                        text_hatchback_count.text = pContext.getString(
                            R.string.vehicle_count,
                            hatchbacks,
                            pContext.resources.getQuantityString(R.plurals.hatchback, hatchbacks)
                        )
                        text_sedan_count.text = pContext.getString(
                            R.string.vehicle_count,
                            sedans,
                            pContext.resources.getQuantityString(R.plurals.sedan, sedans)
                        )
                        text_suv_count.text = pContext.getString(
                            R.string.vehicle_count,
                            suvs,
                            pContext.resources.getQuantityString(R.plurals.suv, suvs)
                        )
                        text_vehicles.text = pContext.resources.getQuantityString(R.plurals.vehicle, totalCount)
                    }
                }
                is Failure -> {
                    veil_vehicles.veil()
                    pContext.toast(it.message)
                }
            }
        }

        recycler_vehicles.apply {
            viewModel.vehicleMinimalsResult.observe(viewLifecycleOwner) {
                when(it) {
                    is Loading -> veil_vehicles.veil()
                    is Success<*> -> {
                        veil_vehicles.unVeil()
                        val minimalReturn = it.data!! as MinimalReturn
                        setAdapter(
                            VehicleListAdapter(
                                pContext,
                                minimalReturn.first,
                                minimalReturn.second
                            )
                        )
                    }
                    is Failure -> {
                        veil_vehicles.veil()
                        pContext.toast(it.message)
                    }
                }
            }
            if (expandableListAdapter is VehicleListAdapter) {
                setViewHeight(-1)
            }
        }
    }

    private fun setupListeners() {
        recycler_vehicles.apply {
            setOnGroupClickListener { parent, _, groupPosition, _ ->
                parent.setViewHeight(groupPosition)
                false
            }

            setOnChildClickListener { _, v, _, _, _ ->
                val vin: String = v.findViewById<MaterialTextView>(R.id.text_vehicle_vin).text
                    .toString()
                    .substring(5)
                    .trim()
                val intent = Intent(pContext, VehicleDetailsActivity::class.java).apply {
                    putExtra(EXTRA_EMAIL, email)
                    putExtra(EXTRA_ROLE, role)
                    putExtra(EXTRA_VIN, vin)
                }
                startActivity(intent)
                true
            }
        }
    }

    private fun setupBadge() {
        viewModel.alertCountResult.observe(viewLifecycleOwner) {
            if (it == 0) {
                alertCountTextView.visibility = View.GONE
            } else {
                alertCountTextView.text = it.toString()
                alertCountTextView.visibility = View.VISIBLE
            }
        }
    }
}
