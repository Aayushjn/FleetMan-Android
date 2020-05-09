package com.aayush.fleetmanager.ui.fragment.login

import android.content.Context
import androidx.lifecycle.*
import com.aayush.fleetmanager.App
import com.aayush.fleetmanager.api.repository.UserRepository
import com.aayush.fleetmanager.di.component.DaggerViewModelComponent
import com.aayush.fleetmanager.di.component.ViewModelComponent
import com.aayush.fleetmanager.di.module.AppModule
import com.aayush.fleetmanager.di.module.RoomModule
import com.aayush.fleetmanager.util.common.State
import javax.inject.Inject

class LoginViewModel(context: Context): ViewModel() {
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

    fun getLoginResult(email: String, password: String): LiveData<State> = userRepository
        .loginUser(email, password)
        .asLiveData(viewModelScope.coroutineContext)
}

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(context) as T
        } else {
            throw IllegalArgumentException("ViewModel not found!")
        }
    }
}