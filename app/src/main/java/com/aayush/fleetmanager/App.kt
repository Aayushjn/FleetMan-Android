package com.aayush.fleetmanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import com.aayush.fleetmanager.util.common.CHANNEL_ALERTS
import com.aayush.fleetmanager.util.common.CHANNEL_ALERTS_ID
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class App: Application() {
    override fun onCreate() {
        super.onCreate()

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
                setShowBadge(true)
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(this)
            }
        }
    }
}