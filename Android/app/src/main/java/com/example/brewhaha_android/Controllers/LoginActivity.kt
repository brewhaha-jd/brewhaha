package com.example.brewhaha_android.Controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.brewhaha_android.Api.BackendConnection
import com.example.brewhaha_android.R
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import com.example.brewhaha_android.Models.LoginUser
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class LoginActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity() {

    var _register_button: Button? = null
    var _login_button: Button? = null
    var _email_text: EditText? = null
    var _password_text: EditText? = null

    //TODO: make the user automatically login if a refresh token is saved
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        _register_button = findViewById<MaterialButton>(R.id.registerButton)
        _login_button = findViewById<MaterialButton>(R.id.loginButton)
        _email_text = findViewById<TextInputEditText>(R.id.emailText)
        _password_text = findViewById<TextInputEditText>(R.id.passwordText)

        _register_button!!.setOnClickListener {
            registerButtonClicked()
        }
        _login_button!!.setOnClickListener{
            loginButtonClicked()
        }
    }

    fun loginButtonClicked() {
        var username = _email_text?.text.toString()
        var password = _password_text?.text.toString()

        if (!validate(username, password)) {
            toast("You need to provide a username and password!")
        }

        val user = LoginUser(username, password)
        sendLoginRequest(user)
    }

    fun registerButtonClicked() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun sendLoginRequest(loginUser: LoginUser) {
        Log.d("Login", "Making login Request")
        doAsync {
            val call = api.login(loginUser).execute()
            if (call.isSuccessful) {
                val token = call.body()
                uiThread {
                    Log.d("Login", "Succesful")

                    val sharedPref = getSharedPreferences("BREWHAHA_PREF", Context.MODE_PRIVATE)
                    var editor = sharedPref.edit()
                    editor.putString("token", token.token)
                    editor.putString("refreshToken", token.refreshToken)
                    editor.commit()
                    //TODO: when the user logs out clear this

                    var bundle = bundleOf("token" to token.token, "refreshToken" to token.refreshToken)
                    val intent = Intent(baseContext, HomeActivity::class.java)
                    intent.putExtra("bundle", bundle)
                    startActivity(intent)
                }
            } else {
                val error = when (call.code()) {
                    400 -> "Bad Request"
                    401 -> "Invalid credentials"
                    404 -> "Resource not found"
                    409 -> "You are already logged in on another device"
                    500 -> "Something went wrong, try again!"
                    else -> "Make sure you are connected to the internet and try again!"

                }
                uiThread {
                    toast(error)
                    Log.d("Login Error", call.code().toString())
                }
            }
        }
    }

    fun validate(username: String, password: String) : Boolean {
        if (username.length == 0 || password.length == 0) return false
        return true
    }
}