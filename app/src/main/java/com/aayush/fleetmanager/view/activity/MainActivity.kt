package com.aayush.fleetmanager.view.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.util.android.BaseActivity
import com.aayush.fleetmanager.util.android.FragmentFactoryImpl
import com.aayush.fleetmanager.util.android.getLoggedInUserEmailAndRole
import com.aayush.fleetmanager.util.android.isUserLoggedIn
import com.aayush.fleetmanager.util.common.*
import com.aayush.fleetmanager.view.fragment.DashboardFragment
import com.aayush.fleetmanager.view.fragment.LoginFragment

class MainActivity: BaseActivity() {
    private val sharedPreferences: SharedPreferences by lazy(LazyThreadSafetyMode.NONE) {
        getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = FragmentFactoryImpl()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isUserLoggedIn(sharedPreferences)) {
            supportFragmentManager.commit {
                replace<LoginFragment>(R.id.fragment_container, TAG_LOGIN_FRAGMENT)
            }
        } else {
            val (email: String, role: String) = getLoggedInUserEmailAndRole(sharedPreferences)
            supportFragmentManager.commit {
                replace<DashboardFragment>(
                    R.id.fragment_container,
                    TAG_DASHBOARD_FRAGMENT,
                    Bundle().apply {
                        putString(EXTRA_EMAIL, email)
                        putString(EXTRA_ROLE, role)
                    }
                )
            }
        }
    }
}
