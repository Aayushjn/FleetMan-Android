package com.aayush.fleetmanager.ui.register

import android.content.Context
import androidx.lifecycle.*
import com.aayush.fleetmanager.App
import com.aayush.fleetmanager.api.repository.UserRepository
import com.aayush.fleetmanager.di.component.DaggerViewModelComponent
import com.aayush.fleetmanager.di.component.ViewModelComponent
import com.aayush.fleetmanager.di.module.AppModule
import com.aayush.fleetmanager.di.module.RoomModule
import com.aayush.fleetmanager.util.common.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RegisterViewModel(context: Context): ViewModel() {
    private val component: ViewModelComponent by lazy {
        DaggerViewModelComponent.builder()
            .appModule(AppModule(context.applicationContext as App))
            .roomModule(RoomModule(context.applicationContext as App))
            .build()
    }

    @Inject lateinit var userRepository: UserRepository

    init {
        component.inject(this)
    }

    fun getRegistrationResult(name: String, email: String, password: String, role: String): LiveData<State> =
        userRepository
            .registerUser(name, email, password, role)
            .asLiveData(viewModelScope.coroutineContext)
}

@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class RegisterViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            RegisterViewModel(context) as T
        } else {
            throw IllegalArgumentException("ViewModel not found!")
        }
    }
}