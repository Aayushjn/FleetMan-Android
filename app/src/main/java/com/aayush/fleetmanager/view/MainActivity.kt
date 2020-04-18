package com.aayush.fleetmanager.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aayush.fleetmanager.App
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.databinding.ActivityMainBinding
import com.aayush.fleetmanager.di.component.ActivityComponent
import com.aayush.fleetmanager.di.component.DaggerActivityComponent
import com.aayush.fleetmanager.di.module.AppModule
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val component: ActivityComponent by lazy(LazyThreadSafetyMode.NONE) {
        DaggerActivityComponent.builder()
            .appModule(AppModule(applicationContext as App))
            .build()
    }

    @Inject lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment)

        binding.toolbar.setupWithNavController(
            navController,
            AppBarConfiguration(
                setOf(
                    R.id.navigation_register,
                    R.id.navigation_login,
                    R.id.navigation_dashboard
                )
            )
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_dashboard -> supportActionBar?.title = getString(R.string.title_dashboard)
                R.id.navigation_login -> supportActionBar?.title = getString(R.string.title_login)
                R.id.navigation_register -> supportActionBar?.title = getString(R.string.title_register)
                R.id.navigation_vehicle_details -> supportActionBar?.title = getString(R.string.title_vehicle_details)
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) =
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
}
