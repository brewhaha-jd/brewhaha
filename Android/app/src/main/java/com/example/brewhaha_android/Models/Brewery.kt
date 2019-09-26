package com.example.brewhaha_android.Models

data class Brewery(
    val name: String,
    val address: Address? = null,
    val friendlinessRating: FriendlinessRating? = null,
    val operatingHours: List<String>? = null,
    val website: String? = null,
    val _id: String? = null,
    val __v: String? = null
)

data class Address(
    val location: BreweryLocation,
    val number: Int,
    val line1: String,
    val line2: String,
    val line3: String,
    val stateOrProvince: String,
    val county: String,
    val country: String,
    val postalCode: String,
    val telephone: String
)

data class FriendlinessRating(
    val aggregate: Double? = null,
    val kidsFood: Double? = null,
    val kidsEntertainment: Double? = null,
    val bathrooms: Double? = null,
    val minRecommendedAge: Int? = null
)

data class BreweryLocation(
    val coordinates: List<Float>,
    val type: String
)

data class AddBrewery(
    val address: Address,
    val name: String,
    val website: String
)

enum class RatingType {
    aggregate,
    kidsFood,
    kidsEntertainment,
    bathrooms,
    minReccomendedAge
}

