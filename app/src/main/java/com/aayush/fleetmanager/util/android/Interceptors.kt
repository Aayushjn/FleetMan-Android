package com.aayush.fleetmanager.util.android

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class CacheInterceptor(private val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        if (isNetworkNotAvailable(context)) {
            val maxStale = 60 * 60 * 24 * 28
            request = request.newBuilder()
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=$maxStale"
                )
                .build()
        }
        return chain.proceed(request)
    }
}