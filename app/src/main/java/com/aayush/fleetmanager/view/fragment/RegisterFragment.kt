package com.aayush.fleetmanager.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.observe
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.api.repository.UserRepository
import com.aayush.fleetmanager.model.Role
import com.aayush.fleetmanager.util.android.*
import com.aayush.fleetmanager.util.common.*
import com.aayush.fleetmanager.view.activity.MainActivity
import com.aayush.fleetmanager.viewmodel.RegistrationViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.progress_layout.*

class RegisterFragment: BaseFragment() {
    private var name: String? = null
    private var email: String? = null
    private var role: Role? = null
    private var password: String? = null
    private var rePassword: String? = null

    private lateinit var viewModel: RegistrationViewModel

    private val pContext: Context by lazy(LazyThreadSafetyMode.NONE) { requireParentContext() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (pContext as MainActivity).supportActionBar?.title = "Register"

        ArrayAdapter(
            pContext,
            R.layout.material_spinner_item,
            pContext.resources.getStringArray(R.array.roles_array)
        ).also {
            spinner_role.setAdapter(it)
        }
        setupListeners()
    }

    private fun setupListeners() {
        edit_re_pass.addTextChangedListener {
            if (it.toString() != edit_pass.asString()) {
                edit_re_pass.error = "Passwords must match!"
            } else {
                edit_re_pass.error = null
            }
        }

        edit_email.addTextChangedListener {
            if (!Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()) {
                edit_email.error = "Must be a valid email ID"
            } else {
                edit_email.error = null
            }
        }

        btn_register.setOnClickListener {
            if (edit_name.isBlankOrEmpty()) {
                edit_name.error = "Name is required"
            } else {
                edit_name.error = null
            }
            if (spinner_role.isBlankOrEmpty()) {
                spinner_role.error = "Select a role"
            } else {
                spinner_role.error = null
            }
            if (edit_email.isBlankOrEmpty()) {
                edit_email.error = "Email ID required"
            } else {
                edit_email.error = null
            }
            if (edit_pass.isBlankOrEmpty()) {
                edit_pass.error = "Password required"
            } else {
                edit_pass.error = null
            }
            if (edit_re_pass.isBlankOrEmpty()) {
                edit_re_pass.error = "Re-enter password"
            } else {
                edit_re_pass.error = null
            }

            name = if (edit_name.isBlankOrEmpty()) null else edit_name.asString()
            email = if (edit_email.isBlankOrEmpty()) null else edit_email.asString()
            role = if (spinner_role.isBlankOrEmpty()) null else Role.valueOf(spinner_role.asString().toUpperCase(DEFAULT_LOCALE))
            password = if (edit_pass.isBlankOrEmpty()) null else edit_pass.asString()
            rePassword = if (edit_re_pass.isBlankOrEmpty()) null else edit_re_pass.asString()

            if (name != null && email != null && role != null && password != null && rePassword != null && password == rePassword) {
                viewModel = RegistrationViewModel(
                    UserRepository.getInstance(pContext.getApp().restApi),
                    name!!,
                    email!!,
                    password!!,
                    role.toString()
                )
                viewModel.registrationResult.observe(viewLifecycleOwner) {
                    when(it) {
                        is Success<*> -> {
                            progress_wheel.visibility = View.GONE
                            with((pContext as MainActivity)) {
                                with(supportFragmentManager) {
                                    popBackStack()
                                    commit {
                                        replace<DashboardFragment>(
                                            R.id.fragment_container,
                                            TAG_DASHBOARD_FRAGMENT,
                                            Bundle().apply {
                                                putString(EXTRA_EMAIL, email)
                                                putString(EXTRA_ROLE, role.toString())
                                            }
                                        )
                                    }
                                }
                                loginUser(
                                    getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE),
                                    email!!,
                                    role.toString()
                                )
                            }
                        }
                        is Failure -> {
                            progress_wheel.visibility = View.GONE
                            pContext.toast(it.message)
                        }
                        is Loading -> progress_wheel.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}