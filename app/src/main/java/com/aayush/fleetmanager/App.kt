package com.aayush.fleetmanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import com.aayush.fleetmanager.api.RestApi
import com.aayush.fleetmanager.api.RestApiFactory
import com.aayush.fleetmanager.db.AppDatabase
import com.aayush.fleetmanager.util.android.getNotificationManager
import com.aayush.fleetmanager.util.common.CHANNEL_ALERTS
import com.aayush.fleetmanager.util.common.CHANNEL_ALERTS_ID
import com.jakewharton.threetenabp.AndroidThreeTen
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class App: Application() {
    val restApi: RestApi by lazy { RestApiFactory.create(this) }
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/montserrat/Montserrat-Regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(CHANNEL_ALERTS_ID, CHANNEL_ALERTS, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Alerts Channel"
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                setShowBadge(true)
                getNotificationManager().createNotificationChannel(this)
            }
        }
    }
}