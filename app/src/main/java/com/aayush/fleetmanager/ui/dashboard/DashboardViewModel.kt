package com.aayush.fleetmanager.ui.dashboard

import android.content.Context
import androidx.lifecycle.*
import com.aayush.fleetmanager.App
import com.aayush.fleetmanager.api.repository.UserRepository
import com.aayush.fleetmanager.api.repository.VehicleRepository
import com.aayush.fleetmanager.db.dao.AlertDao
import com.aayush.fleetmanager.di.component.DaggerViewModelComponent
import com.aayush.fleetmanager.di.component.ViewModelComponent
import com.aayush.fleetmanager.di.module.AppModule
import com.aayush.fleetmanager.di.module.RoomModule
import com.aayush.fleetmanager.model.Alert
import com.aayush.fleetmanager.model.VehicleMinimal
import com.aayush.fleetmanager.util.common.State
import com.aayush.fleetmanager.util.common.State.Loading
import com.aayush.fleetmanager.util.common.State.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

typealias MinimalReturn = Pair<List<String>, HashMap<String, List<VehicleMinimal>>>

@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class DashboardViewModel(context: Context): ViewModel() {
    private val component: ViewModelComponent by lazy {
        DaggerViewModelComponent.builder()
            .appModule(AppModule(context.applicationContext as App))
            .roomModule(RoomModule(context.applicationContext as App))
            .build()
    }

    @Inject lateinit var vehicleRepository: VehicleRepository
    @Inject lateinit var userRepository: UserRepository
    @Inject lateinit var alertDao: AlertDao

    val alertCountResult: Flow<Int>
        get() = alertDao.getAlertCountDistinctUntilChanged()

    val alertResult: Flow<List<Alert>>
        get() = alertDao.getAllAlertsDistinctUntilChanged()

    private val _vehicleMinimalsResult: LiveData<State>

    private val _vehicleCountResult: LiveData<State>

    val vehicleMinimalsResult: LiveData<State>
        get() = _vehicleMinimalsResult

    val vehicleCountResult: LiveData<State>
        get() = _vehicleCountResult

    fun getLogoutResult(email: String): LiveData<State> = userRepository
        .logoutUser(email)
        .asLiveData(viewModelScope.coroutineContext)

    init {
        component.inject(this)

        _vehicleMinimalsResult = vehicleRepository.loadVehicleMinimals()
            .map {
                if (it is Success<*>) {
                    Success(
                        listOf(
                            "Hatchbacks",
                            "Sedans",
                            "SUVs"
                        ) to (it.data!! as HashMap<String, List<VehicleMinimal>>)
                    )
                } else {
                    Loading
                }
            }
            .asLiveData(Dispatchers.Default + viewModelScope.coroutineContext)

        _vehicleCountResult = vehicleRepository
            .loadVehicleCount()
            .asLiveData(viewModelScope.coroutineContext)
    }
}

@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class DashboardViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            DashboardViewModel(context) as T
        } else {
            throw IllegalArgumentException("ViewModel not found!")
        }
    }
}