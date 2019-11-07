package com.example.brewhaha_android.Api

import com.example.brewhaha_android.Models.*
import retrofit2.Call
import retrofit2.http.*

interface BackendInterface {
    @GET("user")
    fun getAllUsers(@Header("x-access-token") accessToken: String) : Call<User>

    @GET("user/{id}")
    fun getUser(@Header("x-access-token") accessToken: String, @Path("id") id: String) : Call<User>

    @POST("auth/createUser")
    fun registerUser(@Header("Content-type") contentType: String, @Body user: UserWithPassword) : Call<User>

    @POST("auth/login")
    fun login(@Header("Content-type") contentType: String, @Body user: LoginUser) : Call<AuthToken>

    @POST("auth/logout")
    fun logout(@Header("Content-type") contentType: String, @Body user: LogoutUser) : Call<Void>

    @GET("brewery")
    fun getAllBreweries(@Header("x-access-token") accessToken: String) : Call<List<Brewery>>

    @GET("brewery/{id}")
    fun getBrewery(@Header("x-access-token") accessToken: String, @Path("id") id: String) : Call<Brewery>

    @POST("brewery/{id}")
    fun updateBrewery(@Header("x-access-token") accessToken: String, @Header("Content-type") contentType: String, @Path("id") id: String, @Body brewery: AddBrewery) : Call<Brewery>

    @PUT("brewery")
    fun addBrewery(@Header("x-access-token") accessToken: String, @Header("Content-type") contentType: String, @Body brewery: AddBrewery) : Call<Map<String, String>>

    @GET("brewery")
    fun filterBreweries(@Header("x-access-token") accessToken: String, @QueryMap queryMap: Map<String, String>) : Call<List<Brewery>>
}