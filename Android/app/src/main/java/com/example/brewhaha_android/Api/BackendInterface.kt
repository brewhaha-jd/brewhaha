package com.example.brewhaha_android.Api

import com.example.brewhaha_android.Models.AuthToken
import com.example.brewhaha_android.Models.LoginUser
import com.example.brewhaha_android.Models.User
import com.example.brewhaha_android.Models.UserWithPassword
import retrofit2.Call
import retrofit2.http.*

interface BackendInterface {
    @GET("api/user")
    fun getAllUsers(@Header("x-access-token") accessToken: String, @Path("id") id: Int) : Call<User>

    @GET("api/user/{id}")
    fun getUser(@Header("x-access-token") accessToken: String, @Path("id") id: Int) : Call<User>

    @POST("auth/createUser")
    fun registerUser(@Header("Content-type") contentType: String, @Body user: UserWithPassword) : Call<String>

    @POST("auth/login")
    fun login(@Header("Content-type") contentType: String, @Body user: LoginUser) : Call<AuthToken>
}