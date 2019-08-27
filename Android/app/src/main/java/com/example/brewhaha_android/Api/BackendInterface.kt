package com.example.brewhaha_android.Api

import com.example.brewhaha_android.Models.User
import retrofit2.Call
import retrofit2.http.*

interface BackendInterface {
    @GET("/user/{id}")
    fun getUser(@Path("id") id: Int) : Call<User>

    @Headers("Content-type: application/json")
    @POST("/users")
    fun registerUser(@Body user: String) : Call<String>

}