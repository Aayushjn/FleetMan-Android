package com.aayush.fleetmanager.util.android.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.model.Alert
import com.aayush.fleetmanager.model.AlertType
import com.aayush.fleetmanager.util.android.getApp
import com.aayush.fleetmanager.util.android.getLoggedInUserEmailAndRole
import com.aayush.fleetmanager.util.android.getNotificationManager
import com.aayush.fleetmanager.util.common.*
import com.aayush.fleetmanager.view.activity.VehicleDetailsActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class FirebaseService: FirebaseMessagingService() {
    override fun onNewToken(s: String) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    Timber.w(it.exception, "getInstanceId() failed")
                    return@addOnCompleteListener
                }
                val token = it.result?.token
                val email = getSharedPreferences(
                    PREF_FILE_KEY,
                    Context.MODE_PRIVATE
                ).getString(PREF_EMAIL, "")!!
                runBlocking(Dispatchers.IO) {
                    getApp().restApi.registerToken(email, token ?: "")
                }
            }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val alert: Alert? = Alert.fromMap(remoteMessage.data)
        if (alert != null) {
            if (getLoggedInUserEmailAndRole(
                    getSharedPreferences(
                        PREF_FILE_KEY,
                        Context.MODE_PRIVATE
                    )
                ).first == remoteMessage.data["email_id"]
            ) {
                runBlocking(Dispatchers.IO) {
                    getApp().database.alertDao().insert(alert)
                }

                if (alert.type == AlertType.SEVERE) {
                    val notificationIntent: Intent =
                        Intent(this, VehicleDetailsActivity::class.java).apply {
                            putExtra(EXTRA_ALERT_ID, alert.id)
                            putExtra(EXTRA_VIN, alert.vin)
                            putExtra(EXTRA_EMAIL, remoteMessage.data["email_id"])
                            putExtra(EXTRA_ROLE, remoteMessage.data["role"])
                        }
                    val deleteIntent: Intent = Intent(this, DeleteAlertService::class.java).apply {
                        putExtra(EXTRA_ALERT_ID, alert.id)
                    }

                    val notificationManager: NotificationManager = getNotificationManager()
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
                                .setContentIntent(
                                    PendingIntent.getActivity(
                                        this,
                                        alert.id.hashCode(),
                                        notificationIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                    )
                                )
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
                                .setContentIntent(
                                    PendingIntent.getActivity(
                                        this,
                                        alert.id.hashCode(),
                                        notificationIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                    )
                                )
                                .build()
                        }
                    notificationManager.notify(100, notification)
                }
            }
        }
    }
}