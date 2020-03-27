package com.aayush.fleetmanager.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.aayush.fleetmanager.api.repository.VehicleRepository
import com.aayush.fleetmanager.db.AppDatabase
import com.aayush.fleetmanager.model.Alert
import com.aayush.fleetmanager.model.VehicleMinimal
import com.aayush.fleetmanager.util.android.getApp
import com.aayush.fleetmanager.util.common.Loading
import com.aayush.fleetmanager.util.common.Result
import com.aayush.fleetmanager.util.common.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import java.util.*

typealias MinimalReturn = Pair<List<String>, HashMap<String, List<VehicleMinimal>>>

@Suppress("UNCHECKED_CAST")
class DashboardViewModel(context: Context) : ViewModel() {
    private val vehicleRepository: VehicleRepository =
        VehicleRepository.getInstance(context.getApp().restApi)
    private val database: AppDatabase = context.getApp().database

    val alertCountResult: LiveData<Int>
        get() = liveData(viewModelScope.coroutineContext) {
            emitSource(database.alertDao().getNumberOfAlerts())
        }

    val alertResult: LiveData<List<Alert>>
        get() = liveData(viewModelScope.coroutineContext) {
            emitSource(database.alertDao().getAllAlerts())
        }

    private val _vehicleMinimalsResult: LiveData<Result> =
        VehicleRepository.getInstance(context.getApp().restApi)
            .loadVehicleMinimals()
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

    private val _vehicleCountResult: LiveData<Result> =
        vehicleRepository
            .loadVehicleCount()
            .asLiveData(viewModelScope.coroutineContext)

    val vehicleMinimalsResult: LiveData<Result>
        get() = _vehicleMinimalsResult

    val vehicleCountResult: LiveData<Result>
        get() = _vehicleCountResult
}

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