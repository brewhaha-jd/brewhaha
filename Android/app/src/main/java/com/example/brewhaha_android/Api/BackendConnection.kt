package com.example.brewhaha_android.Api

import com.example.brewhaha_android.Models.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class BackendConnection {

    private val backendApi: BackendInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("TODO: REPLACE STRING")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        backendApi = retrofit.create(BackendInterface::class.java)
    }

    fun login(username: String, password: String) : Call<User> {
        return backendApi.loginUser(username)
    }
}