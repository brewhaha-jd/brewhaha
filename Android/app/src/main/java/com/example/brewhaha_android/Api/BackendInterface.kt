package com.example.brewhaha_android.Api

import com.example.brewhaha_android.Models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BackendInterface {
    @GET("/users")
    fun loginUser(@Query("username") username: String) : Call<User>
}