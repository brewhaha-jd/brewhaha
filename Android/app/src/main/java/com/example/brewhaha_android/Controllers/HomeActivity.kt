package com.example.brewhaha_android.Controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.brewhaha_android.Api.BackendConnection
import com.example.brewhaha_android.Models.LogoutUser
import com.example.brewhaha_android.R
import com.google.android.material.button.MaterialButton
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class HomeActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity() {
    var _token_text: TextView? = null
    var _refreshToken_text : TextView? = null
    var _id_text : TextView? = null
    var _logout_button : MaterialButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        _token_text = findViewById<TextView>(R.id.tokenTextView)
        _refreshToken_text = findViewById<TextView>(R.id.refreshTokenTextView)
        _id_text = findViewById(R.id.userIdTextView)
        _logout_button = findViewById<MaterialButton>(R.id.logoutButton)

        var tokenBundle = intent.getBundleExtra("bundle")
        var token = tokenBundle["token"] as String
        var refreshToken = tokenBundle["refreshToken"] as String
        var userId = tokenBundle["id"] as String

        _token_text!!.text = token
        _refreshToken_text!!.text = refreshToken
        _id_text!!.text = userId

        _logout_button!!.setOnClickListener{
            logout(userId)
        }

    }

    fun logout(userId: String) {
        val logoutUser = LogoutUser(userId)
        Log.d("Logout", "Logging out: " + userId)
        doAsync {
            val response = api.logout(logoutUser).execute()
            if (response.isSuccessful) {
                uiThread {
                    toast("Logged Out")
                    val intent = Intent(baseContext, LoginActivity::class.java)
                    startActivity(intent)
                }
            } else {
                val error = when (response.code()) {
                    400 -> "Bad Request"
                    401 -> "Invalid credentials"
                    404 -> "Resource not found"
                    409 -> "You are already logged in on another device"
                    500 -> "Something went wrong, try again!"
                    else -> "Make sure you are connected to the internet and try again!"

                }
                uiThread {
                    toast(error)
                    Log.d("Login Error", response.code().toString())
                }
            }
        }
    }
}