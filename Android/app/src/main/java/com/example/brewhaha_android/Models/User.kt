package com.example.brewhaha_android.Models

class Name(
    val firstName: String,
    val lastName: String
)

class BreweryManager(
    val isManager: Boolean,
    val brewery: String
)

class User(
    val username: String,
    val name: Name,
    val email: String,
    val breweryManager: BreweryManager,
    val location: String
)