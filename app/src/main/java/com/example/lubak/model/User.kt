package com.example.lubak.model

class User(
    private val id: Int,
    var username: String,
    var email: String,
) {

    fun updateEmail(newEmail: String) {
        email = newEmail
    }

    override fun toString(): String {
        return "User(id=$id, username='$username', email='$email')"
    }
}
