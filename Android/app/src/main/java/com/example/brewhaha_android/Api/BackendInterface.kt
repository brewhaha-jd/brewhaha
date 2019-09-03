package com.example.brewhaha_android.Api

import com.example.brewhaha_android.Models.AuthToken
import com.example.brewhaha_android.Models.User
import retrofit2.Call
import retrofit2.http.*

interface BackendInterface {
    @GET("/api/user")
    fun getAllUsers(@Header("x-access-token") accessToken: String, @Path("id") id: Int) : Call<User>

    @GET("/api/user/{id}")
    fun getUser(@Header("x-access-token") accessToken: String, @Path("id") id: Int) : Call<User>

    @Headers("Content-type: application/json")
    @POST("/auth/users/createUser")
    fun registerUser(@Body user: String) : Call<String>

    @Headers("Content-type: application/json")
    @POST("/auth/users/login")
    fun login(@Body user: String) : Call<AuthToken>

}