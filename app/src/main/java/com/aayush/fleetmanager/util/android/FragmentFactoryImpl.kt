package com.aayush.fleetmanager.util.android

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.aayush.fleetmanager.view.fragment.DashboardFragment
import com.aayush.fleetmanager.view.fragment.LoginFragment
import com.aayush.fleetmanager.view.fragment.RegisterFragment
import com.aayush.fleetmanager.view.fragment.VehicleDetailsFragment

class FragmentFactoryImpl: FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            LoginFragment::class.java.name -> LoginFragment()
            RegisterFragment::class.java.name -> RegisterFragment()
            DashboardFragment::class.java.name -> DashboardFragment()
            VehicleDetailsFragment::class.java.name -> VehicleDetailsFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}