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

    @Headers("{Content-type: application/json}")
    @POST("auth/createUser")
    fun registerUser(@Body user: UserWithPassword) : Call<String>

    @Headers("{Content-type: application/json}")
    @POST("auth/login")
    fun login(@Body user: LoginUser) : Call<AuthToken>
}