package com.aayush.fleetmanager.util.android

import android.content.SharedPreferences
import androidx.core.content.edit
import com.aayush.fleetmanager.util.common.PREF_EMAIL
import com.aayush.fleetmanager.util.common.PREF_IS_LOGGED_IN
import com.aayush.fleetmanager.util.common.PREF_ROLE

fun loginUser(sharedPreferences: SharedPreferences, emailId: String, role: String) =
    sharedPreferences.edit {
        putString(PREF_EMAIL, emailId)
        putString(PREF_ROLE, role)
        putBoolean(PREF_IS_LOGGED_IN, true)
    }

fun logoutUser(sharedPreferences: SharedPreferences) = sharedPreferences.edit {
    putString(PREF_EMAIL, "")
    putString(PREF_ROLE, "")
    putBoolean(PREF_IS_LOGGED_IN, false)
}

fun isUserLoggedIn(sharedPreferences: SharedPreferences): Boolean =
    sharedPreferences.getBoolean(PREF_IS_LOGGED_IN, false)

fun getLoggedInUserEmailAndRole(sharedPreferences: SharedPreferences): Pair<String, String> =
    sharedPreferences.getString(PREF_EMAIL, "")!! to sharedPreferences.getString(PREF_ROLE, "")!!