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
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class HomeActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity() {
    var _logout_button : MaterialButton? = null
    var _view_brewery_button : MaterialButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        _logout_button = findViewById<MaterialButton>(R.id.logoutButton)
        _view_brewery_button = findViewById(R.id.viewBreweries)

        var tokenBundle = intent.getBundleExtra("bundle")
        var token = tokenBundle["token"] as String
        var refreshToken = tokenBundle["refreshToken"] as String
        var userId = tokenBundle["id"] as String

        _logout_button!!.setOnClickListener{
            logout(userId)
        }

        _view_brewery_button!!.setOnClickListener{
            viewBreweries(tokenBundle)
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

    fun viewBreweries(tokenBundle: Bundle) {
        val intent = Intent(baseContext, ViewBreweryActivity::class.java)
        intent.putExtra("bundle", tokenBundle)
        startActivity(intent)
    }
}