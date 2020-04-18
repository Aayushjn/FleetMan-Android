package com.aayush.fleetmanager.di.module

import android.content.Context
import android.net.ConnectivityManager
import com.aayush.fleetmanager.App
import com.aayush.fleetmanager.api.RestApi
import com.aayush.fleetmanager.api.repository.UserRepository
import com.aayush.fleetmanager.api.repository.VehicleRepository
import com.aayush.fleetmanager.util.android.isNetworkAvailable
import com.aayush.fleetmanager.util.common.BASE_URL
import com.aayush.fleetmanager.util.common.CACHE_SIZE
import com.aayush.fleetmanager.util.common.JSON
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object NetworkModule {
    @Provides
    @Singleton
    fun providesRestApi(app: App): RestApi {
        val cacheInterceptor = object: Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                var request: Request = chain.request()
                val connectivityManager =
                    app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (!connectivityManager.isNetworkAvailable()) {
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

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(JSON.asConverterFactory("application/json".toMediaType()))
            .client(
                OkHttpClient.Builder()
                    .cache(Cache(app.cacheDir, CACHE_SIZE))
                    .addInterceptor(cacheInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(RestApi::class.java)
    }

    @Provides
    @Singleton
    fun providesUserRepository(restApi: RestApi): UserRepository = UserRepository(restApi)

    @Provides
    @Singleton
    fun providesVehicleRepository(restApi: RestApi): VehicleRepository = VehicleRepository(restApi)
}