package com.example.brewhaha_android.Api

import com.example.brewhaha_android.Models.AuthToken
import com.example.brewhaha_android.Models.LoginUser
import com.example.brewhaha_android.Models.User
import com.example.brewhaha_android.Models.UserWithPassword
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class BackendConnection {

    private val backendApi: BackendInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://nickhutch.com:3000/api/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        backendApi = retrofit.create(BackendInterface::class.java)
    }

    fun getUser(token: AuthToken, userId: Int) : Call<User> {
        return backendApi.getUser(token.token, userId)
    }

    fun login(user: LoginUser) : Call<AuthToken> {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        val loginUserJson: String = gson.toJson(user)
        return backendApi.login(loginUserJson)
    }

    fun register(user: UserWithPassword) : Call<String> {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        val userJson: String = gson.toJson(user)
        return backendApi.registerUser(userJson)
    }
}