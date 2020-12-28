package me.codeenzyme.welearn.model

import com.google.firebase.Timestamp

data class User(
    val firstName: String,
    val lastName: String,
    val school: String?,
    val faculty: String?,
    val dept: String?,
    val email: String,
    val subscriptionExpiry: Timestamp?,
) {
    constructor() : this("", "", null, null, null, "", null)
}