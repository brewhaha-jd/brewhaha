package com.example.brewhaha_android.Api

import android.util.Log
import com.example.brewhaha_android.Models.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class BackendConnection {

    private val backendApi: BackendInterface
    private val CONTENT_TYPE: String = "application/json"

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

    fun getUser(token: AuthToken, userId: String) : Call<User> {
        return backendApi.getUser(token.token, userId)
    }

    fun login(user: LoginUser) : Call<AuthToken> {
        return backendApi.login(CONTENT_TYPE, user)
    }

    fun register(user: UserWithPassword) : Call<User> {
        return backendApi.registerUser(CONTENT_TYPE, user)
    }

    fun logout(user: LogoutUser) : Call<Void> {
        return backendApi.logout(CONTENT_TYPE, user)
    }

    fun getAllBreweries(token: AuthToken) : Call<List<Brewery>> {
        return backendApi.getAllBreweries(token.token)
    }

    fun getBreweryById(token: AuthToken, id: String) : Call<Brewery> {
        return backendApi.getBrewery(token.token, id)
    }

    fun getBreweryByName(token: AuthToken, name: String) : Call<List<Brewery>> {
        return backendApi.getBreweryByName(token.token, name)
    }

    fun getBreweryByRating(token: AuthToken, type: RatingType, rating: Float) : Call<List<Brewery>> {
        return backendApi.getBreweryByRating(token.token, type, rating)
    }

    fun getBreweryByLocation(token: AuthToken, location: List<Double>, miles: Double) : Call<List<Brewery>> {
        return backendApi.getBreweryByLocation(token.token, location, miles)
    }
}