package com.example.brewhaha_android.Models

import java.time.LocalDateTime
import java.util.*

data class SubmitReviewModel(
    val user: String,
    val brewery: String,
    val datePosted: Date,
    val friendlinessRating: FriendlinessRating,
    val text: String
)