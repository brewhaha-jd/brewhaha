package com.example.brewhaha_android.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String
)

data class BreweryManager(
    @SerializedName("isManager")
    val isManager: Boolean,
    @SerializedName("brewery")
    val brewery: String
)

data class User(
    @SerializedName("username")
    val username: String,
    @SerializedName("name")
    val name: Name,
    @SerializedName("email")
    val email: String,
    @SerializedName("breweryManager")
    val breweryManager: BreweryManager = BreweryManager(false, ""),
    @SerializedName("location")
    val location: String = ""
)

data class UserWithPassword(
    @SerializedName("username")
    val username: String,
    @SerializedName("name")
    val name: Name,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("breweryManager")
    val breweryManager: BreweryManager = BreweryManager(false, ""),
    @SerializedName("location")
    val location: String = ""
)

data class LoginUser(
    @SerializedName("username")
    @Expose
    val username: String,

    @SerializedName("password")
    @Expose
    val password: String
)

data class LogoutUser(
    @SerializedName("userId")
    @Expose
    val userId: String
)