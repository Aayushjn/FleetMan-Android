package com.aayush.fleetmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aayush.fleetmanager.api.repository.UserRepository
import com.aayush.fleetmanager.util.common.Result

class LogoutViewModel(userRepository: UserRepository, email: String): ViewModel() {
    private val _logoutResult: LiveData<Result> =
        userRepository
            .logoutUser(email)
            .asLiveData(viewModelScope.coroutineContext)

    val logoutResult: LiveData<Result>
        get() = _logoutResult
}