package com.aayush.fleetmanager.view.activity

import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.util.android.BaseActivity
import com.aayush.fleetmanager.util.android.FragmentFactoryImpl
import com.aayush.fleetmanager.util.common.TAG_VEHICLE_DETAILS_FRAGMENT
import com.aayush.fleetmanager.view.fragment.VehicleDetailsFragment

class VehicleDetailsActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = FragmentFactoryImpl()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_details)

        supportActionBar?.apply {
            title = "Vehicle Details"
            setDisplayHomeAsUpEnabled(true)
        }

        supportFragmentManager.commit {
            replace<VehicleDetailsFragment>(
                R.id.fragment_container,
                TAG_VEHICLE_DETAILS_FRAGMENT,
                intent.extras
            )
        }
    }
}