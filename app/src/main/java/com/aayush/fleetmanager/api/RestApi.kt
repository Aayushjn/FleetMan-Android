package com.aayush.fleetmanager.api

import com.aayush.fleetmanager.model.*
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.*

interface RestApi {
    @GET("/vehicles")
    suspend fun getVehicleMinimals(): NetworkResponse<MinimalModel, ErrorResponse>

    @POST("/vehicles")
    @FormUrlEncoded
    suspend fun getVehicleByVinOrLicensePlate(
        @Field("vin") vin: String?,
        @Field("license_plate") licensePlate: String?,
        @Field("email_id") email: String
    ): NetworkResponse<Vehicle, ErrorResponse>

    @GET("/vehicles/count")
    suspend fun getVehicleCount(): NetworkResponse<VehicleCount, ErrorResponse>

    @PUT("/users/create")
    @FormUrlEncoded
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email_id") email: String,
        @Field("password") password: String,
        @Field("role") role: String
    ): NetworkResponse<Unit, ErrorResponse>

    @POST("/users/login")
    @FormUrlEncoded
    suspend fun loginUser(
        @Field("email_id") email: String,
        @Field("password") password: String
    ): NetworkResponse<User, ErrorResponse>

    @POST("/users/logout")
    @FormUrlEncoded
    suspend fun logoutUser(
        @Field("email_id") email: String
    ): NetworkResponse<Unit, ErrorResponse>

    @POST("/token/register")
    @FormUrlEncoded
    suspend fun registerToken(
        @Field("email_id") email: String,
        @Field("token") token: String
    ): NetworkResponse<Unit, ErrorResponse>
}