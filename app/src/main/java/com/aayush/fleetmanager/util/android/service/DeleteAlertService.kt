package com.aayush.fleetmanager.util.android.service

import android.app.IntentService
import android.content.Intent
import android.os.ParcelUuid
import com.aayush.fleetmanager.App
import com.aayush.fleetmanager.db.dao.AlertDao
import com.aayush.fleetmanager.di.component.DaggerServiceComponent
import com.aayush.fleetmanager.di.component.ServiceComponent
import com.aayush.fleetmanager.di.module.AppModule
import com.aayush.fleetmanager.di.module.RoomModule
import com.aayush.fleetmanager.util.common.EXTRA_ALERT_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DeleteAlertService: IntentService("DeleteAlertService") {
    private val component: ServiceComponent by lazy {
        DaggerServiceComponent.builder()
            .appModule(AppModule(applicationContext as App))
            .roomModule(RoomModule(applicationContext as App))
            .build()
    }
    @Inject lateinit var alertDao: AlertDao

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        val id: ParcelUuid = intent?.getParcelableExtra(EXTRA_ALERT_ID)!!

        runBlocking(Dispatchers.IO) { alertDao.deleteAlertById(id) }
    }
}