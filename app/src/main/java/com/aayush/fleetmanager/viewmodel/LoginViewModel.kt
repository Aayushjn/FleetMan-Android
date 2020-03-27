package com.aayush.fleetmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aayush.fleetmanager.api.repository.UserRepository
import com.aayush.fleetmanager.util.common.Result

class LoginViewModel(
    userRepository: UserRepository,
    email: String,
    password: String
): ViewModel() {
    private val _loginResult: LiveData<Result> = userRepository
        .loginUser(email, password)
        .asLiveData(viewModelScope.coroutineContext)

    val loginResult: LiveData<Result>
        get() = _loginResult
}