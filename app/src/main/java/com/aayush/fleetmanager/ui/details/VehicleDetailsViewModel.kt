package com.aayush.fleetmanager.ui.details

import android.content.Context
import androidx.lifecycle.*
import com.aayush.fleetmanager.App
import com.aayush.fleetmanager.api.repository.VehicleRepository
import com.aayush.fleetmanager.di.component.DaggerViewModelComponent
import com.aayush.fleetmanager.di.component.ViewModelComponent
import com.aayush.fleetmanager.di.module.AppModule
import com.aayush.fleetmanager.di.module.RoomModule
import com.aayush.fleetmanager.util.common.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class VehicleDetailsViewModel(context: Context): ViewModel() {
    private val component: ViewModelComponent by lazy(LazyThreadSafetyMode.NONE) {
        DaggerViewModelComponent.builder()
            .appModule(AppModule(context.applicationContext as App))
            .roomModule(RoomModule(context.applicationContext as App))
            .build()
    }
    @Inject lateinit var vehicleRepository: VehicleRepository

    init {
        component.inject(this)
    }

    fun getVehicleResult(vin: String, email: String): LiveData<State> =
        vehicleRepository
        .loadVehicle(vin, email)
        .asLiveData(viewModelScope.coroutineContext)
}

@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class VehicleDetailsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(VehicleDetailsViewModel::class.java)) {
            VehicleDetailsViewModel(context) as T
        } else {
            throw IllegalArgumentException("ViewModel not found!")
        }
    }
}