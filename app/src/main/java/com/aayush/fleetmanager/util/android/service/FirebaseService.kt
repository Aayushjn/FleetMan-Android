package com.aayush.fleetmanager.util.android.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.aayush.fleetmanager.App
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.api.RestApi
import com.aayush.fleetmanager.db.dao.AlertDao
import com.aayush.fleetmanager.di.component.DaggerServiceComponent
import com.aayush.fleetmanager.di.component.ServiceComponent
import com.aayush.fleetmanager.di.module.AppModule
import com.aayush.fleetmanager.di.module.RoomModule
import com.aayush.fleetmanager.model.Alert
import com.aayush.fleetmanager.model.AlertType
import com.aayush.fleetmanager.ui.activity.MainActivity
import com.aayush.fleetmanager.util.android.getLoggedInUserEmailAndRole
import com.aayush.fleetmanager.util.common.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class FirebaseService: FirebaseMessagingService() {
    private val component: ServiceComponent by lazy {
        DaggerServiceComponent.builder()
            .appModule(AppModule(applicationContext as App))
            .roomModule(RoomModule(applicationContext as App))
            .build()
    }
    @Inject lateinit var sharedPreferences: SharedPreferences
    @Inject lateinit var restApi: RestApi
    @Inject lateinit var alertDao: AlertDao

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }

    override fun onNewToken(s: String) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    Timber.w(it.exception, "getInstanceId() failed")
                    return@addOnCompleteListener
                }
                val token = it.result?.token
                val email = sharedPreferences.getString(PREF_EMAIL, "")!!
                runBlocking(Dispatchers.IO) { restApi.registerToken(email, token ?: "") }
            }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val alert: Alert? = Alert.fromMap(remoteMessage.data)
        if (alert != null) {
            if (getLoggedInUserEmailAndRole(sharedPreferences).first == remoteMessage.data["email_id"]) {
                runBlocking(Dispatchers.IO) { alertDao.insert(alert) }

                if (alert.type == AlertType.SEVERE) {
                    val notificationIntent = NavDeepLinkBuilder(this)
                        .setComponentName(MainActivity::class.java)
                        .setGraph(R.navigation.mobile_navigation)
                        .setDestination(R.id.navigation_vehicle_details)
                        .setArguments(
                            bundleOf(
                                EXTRA_ALERT_ID to alert.id,
                                EXTRA_VIN to alert.vin,
                                EXTRA_EMAIL to remoteMessage.data["email_id"],
                                EXTRA_ROLE to remoteMessage.data["role"]
                            )
                        )
                        .createPendingIntent()
                    val deleteIntent: Intent = Intent(this, DeleteAlertService::class.java).apply {
                        putExtra(EXTRA_ALERT_ID, alert.id)
                    }

                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val notification: Notification =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationCompat.Builder(this, CHANNEL_ALERTS_ID)
                                .setContentTitle("Critical alert for ${alert.licensePlate}")
                                .setSmallIcon(R.drawable.ic_error_outline)
                                .setAutoCancel(true)
                                .setDeleteIntent(
                                    PendingIntent.getService(
                                        this,
                                        alert.id.hashCode(),
                                        deleteIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                    )
                                )
                                .setContentIntent(notificationIntent)
                                .build()
                        } else {
                            NotificationCompat.Builder(this)
                                .setContentTitle("Critical alert for ${alert.licensePlate}")
                                .setSmallIcon(R.drawable.ic_error_outline)
                                .setAutoCancel(true)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setDeleteIntent(
                                    PendingIntent.getService(
                                        this,
                                        alert.id.hashCode(),
                                        deleteIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                    )
                                )
                                .setContentIntent(notificationIntent)
                                .build()
                        }
                    notificationManager.notify(100, notification)
                }
            }
        }
    }
}