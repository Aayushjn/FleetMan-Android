package com.aayush.fleetmanager.util.android.service

import android.app.IntentService
import android.content.Intent
import android.os.ParcelUuid
import com.aayush.fleetmanager.util.android.getApp
import com.aayush.fleetmanager.util.common.EXTRA_ALERT_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class DeleteAlertService: IntentService("DeleteAlertService") {
    override fun onHandleIntent(intent: Intent?) {
        val id: ParcelUuid = intent?.getParcelableExtra(EXTRA_ALERT_ID)!!

        runBlocking(Dispatchers.IO) {
            getApp().database.alertDao().deleteAlertById(id)
        }
    }
}