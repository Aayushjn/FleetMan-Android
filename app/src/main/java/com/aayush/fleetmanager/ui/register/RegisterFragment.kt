package com.aayush.fleetmanager.ui.register

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.aayush.fleetmanager.App
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.databinding.FragmentRegisterBinding
import com.aayush.fleetmanager.di.component.DaggerFragmentComponent
import com.aayush.fleetmanager.di.component.FragmentComponent
import com.aayush.fleetmanager.di.module.AppModule
import com.aayush.fleetmanager.di.module.RoomModule
import com.aayush.fleetmanager.model.Role
import com.aayush.fleetmanager.util.android.asString
import com.aayush.fleetmanager.util.android.base.BaseFragment
import com.aayush.fleetmanager.util.android.isBlankOrEmpty
import com.aayush.fleetmanager.util.android.toast
import com.aayush.fleetmanager.util.common.DEFAULT_LOCALE
import com.aayush.fleetmanager.util.common.State.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RegisterFragment: BaseFragment<FragmentRegisterBinding>() {
    private val component: FragmentComponent by lazy(LazyThreadSafetyMode.NONE) {
        DaggerFragmentComponent.builder()
            .appModule(AppModule(requireContext().applicationContext as App))
            .roomModule(RoomModule(requireContext().applicationContext as App))
            .build()
    }
    @Inject lateinit var sharedPreferences: SharedPreferences

    private var name: String? = null
    private var email: String? = null
    private var role: Role? = null
    private var password: String? = null
    private var rePassword: String? = null

    private val registerViewModel: RegisterViewModel by viewModels { RegisterViewModelFactory(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ArrayAdapter(
            requireContext(),
            R.layout.material_spinner_item,
            requireContext().resources.getStringArray(R.array.roles_array)
        ).also {
            binding.spinnerRole.setAdapter(it)
        }
        setupListeners()
    }

    private fun setupListeners() {
        binding.editRePass.addTextChangedListener {
            if (it.toString() != binding.editPass.asString()) {
                binding.editRePass.error = "Passwords must match!"
            } else {
                binding.editRePass.error = null
            }
        }

        binding.editEmail.addTextChangedListener {
            binding.editEmail.error = if (!Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()) {
                "Must be a valid email ID"
            } else {
                null
            }
        }

        binding.btnRegister.setOnClickListener {
            binding.editName.error = if (binding.editName.isBlankOrEmpty()) {
                "Name is required"
            } else {
                null
            }
            binding.spinnerRole.error = if (binding.spinnerRole.isBlankOrEmpty()) {
                "Select a role"
            } else {
                null
            }
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
            binding.editRePass.error = if (binding.editRePass.isBlankOrEmpty()) {
                "Re-enter password"
            } else {
                null
            }

            name = if (binding.editName.isBlankOrEmpty()) null else binding.editName.asString()
            email = if (binding.editEmail.isBlankOrEmpty()) null else binding.editEmail.asString()
            role = if (binding.spinnerRole.isBlankOrEmpty()) null else Role.valueOf(binding.spinnerRole.asString().toUpperCase(DEFAULT_LOCALE))
            password = if (binding.editPass.isBlankOrEmpty()) null else binding.editPass.asString()
            rePassword = if (binding.editRePass.isBlankOrEmpty()) null else binding.editRePass.asString()

            if (name != null && email != null && role != null && password != null && rePassword != null && password == rePassword) {
                registerViewModel.getRegistrationResult(name!!, email!!, password!!, role!!.toString())
                    .observe(viewLifecycleOwner) {
                        when(it) {
                            is Success<*> -> {
                                binding.layoutProgress.progressWheel.visibility = View.GONE
                                findNavController().navigate(RegisterFragmentDirections.navigateToDashboardFragment())
                            }
                            is Failure -> {
                                binding.layoutProgress.progressWheel.visibility = View.GONE
                                requireContext().toast(it.reason)
                            }
                            is Loading -> binding.layoutProgress.progressWheel.visibility = View.VISIBLE
                        }
                    }
            }
        }
    }
}