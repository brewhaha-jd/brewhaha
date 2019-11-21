package com.example.brewhaha_android.Models

import java.time.LocalDateTime
import java.util.*

data class SubmitReviewModel(
    val user: Int,
    val brewery: Int,
    val datePosted: Date,
    val friendlinessRating: FriendlinessRating,
    val text: String
)