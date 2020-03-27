package com.aayush.fleetmanager.api

import android.content.Context
import com.aayush.fleetmanager.util.android.CacheInterceptor
import com.aayush.fleetmanager.util.common.BASE_URL
import com.aayush.fleetmanager.util.common.CACHE_SIZE
import com.aayush.fleetmanager.util.common.JSON
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object RestApiFactory {
    fun create(context: Context): RestApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(JSON.asConverterFactory("application/json".toMediaType()))
            .client(
                OkHttpClient.Builder()
                    .cache(Cache(context.cacheDir, CACHE_SIZE))
                    .addInterceptor(CacheInterceptor(context))
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(RestApi::class.java)
    }
}