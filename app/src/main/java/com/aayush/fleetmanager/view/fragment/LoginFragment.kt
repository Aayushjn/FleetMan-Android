package com.aayush.fleetmanager.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.observe
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.api.repository.UserRepository
import com.aayush.fleetmanager.model.User
import com.aayush.fleetmanager.util.android.*
import com.aayush.fleetmanager.util.common.*
import com.aayush.fleetmanager.view.activity.MainActivity
import com.aayush.fleetmanager.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.progress_layout.*

class LoginFragment: BaseFragment() {
    private var email: String? = null
    private var password: String? = null

    private lateinit var viewModel: LoginViewModel
    
    private val pContext: Context by lazy(LazyThreadSafetyMode.NONE) { requireParentContext() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (pContext as MainActivity).supportActionBar?.title = "Log In"
        setupListeners()
    }

    private fun setupListeners() {
        btn_register.setOnClickListener {
            (pContext as MainActivity).supportFragmentManager.commit {
                setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
                addToBackStack(null)
                replace<RegisterFragment>(R.id.fragment_container, TAG_REGISTER_FRAGMENT)
            }
        }

        edit_email.addTextChangedListener {
            if (!Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()) {
                edit_email.error = "Must be a valid email ID"
            } else {
                edit_email.error = null
            }
        }

        btn_login.setOnClickListener {
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

            email = if (edit_email.isBlankOrEmpty()) null else edit_email.asString()
            password = if (edit_pass.isBlankOrEmpty()) null else edit_pass.asString()

            if (email != null && password != null) {
                viewModel = LoginViewModel(
                    UserRepository.getInstance(pContext.getApp().restApi),
                    email!!,
                    password!!
                )
                viewModel.loginResult.observe(viewLifecycleOwner) {
                    when(it) {
                        is Success<*> -> {
                            progress_wheel.visibility = View.GONE
                            val role: String = (it.data!! as User).role.toString()
                            loginUser(
                                (pContext as MainActivity).getSharedPreferences(
                                    PREF_FILE_KEY,
                                    Context.MODE_PRIVATE
                                ),
                                email!!,
                                role
                            )

                            (pContext as MainActivity).supportFragmentManager.commit {
                                replace<DashboardFragment>(
                                    R.id.fragment_container,
                                    TAG_DASHBOARD_FRAGMENT,
                                    Bundle().apply {
                                        putString(EXTRA_EMAIL, email)
                                        putString(EXTRA_ROLE, role)
                                    }
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