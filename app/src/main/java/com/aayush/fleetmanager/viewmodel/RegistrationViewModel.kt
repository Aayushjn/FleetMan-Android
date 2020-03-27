package com.aayush.fleetmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aayush.fleetmanager.api.repository.UserRepository
import com.aayush.fleetmanager.util.common.Result

class RegistrationViewModel(
    userRepository: UserRepository,
    name: String,
    email: String,
    password: String,
    role: String
): ViewModel() {
    private val _registrationResult: LiveData<Result> = userRepository
        .registerUser(name, email, password, role)
        .asLiveData(viewModelScope.coroutineContext)

    val registrationResult: LiveData<Result>
        get() = _registrationResult
}