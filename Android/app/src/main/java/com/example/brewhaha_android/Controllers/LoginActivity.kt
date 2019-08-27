package com.example.brewhaha_android.Controllers

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.brewhaha_android.Api.BackendConnection
import com.example.brewhaha_android.Models.User
import com.example.brewhaha_android.R
import org.jetbrains.anko.custom.async

class LoginActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginButtonClicked() {
        // TODO: get the username
        // TODO: get the password
        var username = "USERNAME"
        var password = "PASSWORD"
        sendLoginRequest(username, password)
    }

    fun sendLoginRequest(username: String, password: String) {
        var user: User?
        async() {
            val call = api.login(username, password)
            val result = call.execute()
            if (result.isSuccessful) {
                user = result.body()
            } else {
                Toast.makeText(this, result.message(), 40)
            }
        }
    }
}