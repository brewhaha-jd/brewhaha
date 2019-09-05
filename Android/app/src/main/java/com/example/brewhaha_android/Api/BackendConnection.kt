package com.example.brewhaha_android.Api

import com.example.brewhaha_android.Models.AuthToken
import com.example.brewhaha_android.Models.LoginUser
import com.example.brewhaha_android.Models.User
import com.example.brewhaha_android.Models.UserWithPassword
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class BackendConnection {

    private val backendApi: BackendInterface

    init {
        val client = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://nickhutch.com:3000/api/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()

        backendApi = retrofit.create(BackendInterface::class.java)
    }

    fun getUser(token: AuthToken, userId: Int) : Call<User> {
        return backendApi.getUser(token.token, userId)
    }

    fun login(user: LoginUser) : Call<AuthToken> {
        return backendApi.login("application/json", user)
    }

    fun register(user: UserWithPassword) : Call<String> {
        return backendApi.registerUser("application/json", user)
    }
}