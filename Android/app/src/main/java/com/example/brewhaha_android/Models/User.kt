package com.example.brewhaha_android.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Name(
    val firstName: String,
    val lastName: String
)

data class BreweryManager(
    val isManager: Boolean,
    val brewery: String
)

data class User(
    val username: String,
    val name: Name,
    val email: String,
    val breweryManager: BreweryManager? = BreweryManager(false, ""),
    val location: String? = ""
)

data class UserWithPassword(
    val username: String,
    val name: Name,
    val email: String,
    val password: String,
    val breweryManager: BreweryManager? = BreweryManager(false, ""),
    val location: String? = ""
)

data class LoginUser(
    @SerializedName("username")
    @Expose
    val username: String,

    @SerializedName("password")
    @Expose
    val password: String
)