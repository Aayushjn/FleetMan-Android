package com.aayush.fleetmanager.ui.login

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.aayush.fleetmanager.App
import com.aayush.fleetmanager.databinding.FragmentLoginBinding
import com.aayush.fleetmanager.di.component.DaggerFragmentComponent
import com.aayush.fleetmanager.di.component.FragmentComponent
import com.aayush.fleetmanager.di.module.AppModule
import com.aayush.fleetmanager.di.module.RoomModule
import com.aayush.fleetmanager.model.User
import com.aayush.fleetmanager.util.android.asString
import com.aayush.fleetmanager.util.android.base.BaseFragment
import com.aayush.fleetmanager.util.android.isBlankOrEmpty
import com.aayush.fleetmanager.util.android.loginUser
import com.aayush.fleetmanager.util.android.toast
import com.aayush.fleetmanager.util.common.State.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoginFragment: BaseFragment<FragmentLoginBinding>() {
    private val component: FragmentComponent by lazy(LazyThreadSafetyMode.NONE) {
        DaggerFragmentComponent.builder()
            .appModule(AppModule(requireContext().applicationContext as App))
            .roomModule(RoomModule(requireContext().applicationContext as App))
            .build()
    }
    @Inject lateinit var sharedPreferences: SharedPreferences

    private var email: String? = null
    private var password: String? = null

    private val loginViewModel: LoginViewModel by viewModels { LoginViewModelFactory(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.navigateToRegisterFragment())
        }

        binding.editEmail.addTextChangedListener {
            binding.editEmail.error = if (!Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()) {
                "Must be a valid email ID"
            } else {
                null
            }
        }

        binding.btnLogin.setOnClickListener {
            binding.editEmail.error = if (binding.editEmail.isBlankOrEmpty()) {
                "Email ID required"
            } else {
                null
            }
            binding.editPass.error = if (binding.editPass.isBlankOrEmpty()) {
                "Password required"
            } else {
                null
            }

            email = if (binding.editEmail.isBlankOrEmpty()) null else binding.editEmail.asString()
            password = if (binding.editPass.isBlankOrEmpty()) null else binding.editPass.asString()

            if (email != null && password != null) {
                loginViewModel.getLoginResult(email!!, password!!).observe(viewLifecycleOwner) {
                    when(it) {
                        is Success<*> -> {
                            binding.layoutProgress.progressBar.visibility = View.GONE
                            val role: String = (it.data!! as User).role.toString()
                            loginUser(sharedPreferences, email!!, role)

                            findNavController().navigate(LoginFragmentDirections.navigateToDashboardFragment())
                        }
                        is Failure -> {
                            binding.layoutProgress.progressBar.visibility = View.GONE
                            requireContext().toast(it.reason)
                        }
                        is Loading -> binding.layoutProgress.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}