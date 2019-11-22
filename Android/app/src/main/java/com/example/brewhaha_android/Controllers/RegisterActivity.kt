package com.example.brewhaha_android.Controllers

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.brewhaha_android.Api.BackendConnection
import com.example.brewhaha_android.Models.BreweryManager
import com.example.brewhaha_android.Models.LoginUser
import com.example.brewhaha_android.Models.Name
import com.example.brewhaha_android.Models.UserWithPassword
import com.example.brewhaha_android.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.getStackTraceString
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.lang.Exception

class RegisterActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity() {

    var _register_button: MaterialButton? = null
    var _input_email: TextInputEditText? = null
    var _input_password: TextInputEditText? = null
    var _username: TextInputEditText? = null
    var _first_name: TextInputEditText? = null
    var _last_name: TextInputEditText? = null
    var _radio_group: RadioGroup? = null

    var isBreweryManager: Boolean = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        _input_email = findViewById<TextInputEditText>(R.id.email)
        _input_password = findViewById<TextInputEditText>(R.id.password)
        _username = findViewById<TextInputEditText>(R.id.username)
        _first_name = findViewById<TextInputEditText>(R.id.firstName)
        _last_name = findViewById<TextInputEditText>(R.id.lastName)
        _register_button = findViewById<MaterialButton>(R.id.registerButton)
        _radio_group = findViewById<RadioGroup>(R.id.radioGroup)

        _register_button!!.setOnClickListener{ createUser() }
        _radio_group!!.setOnCheckedChangeListener { radioGroup, checkedId ->
            isBreweryManager = (R.id.radioYes == checkedId)
            Log.d("Registration", "Brewery Manager: " + isBreweryManager)
        }

    }

    fun createUser() {
        Log.d("Signup Activity", "CreateUser")

        if (!validate()) {
            onSignupFailed()
            return
        }

        _register_button!!.isEnabled = false

        val progressDialog = ProgressDialog(this)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()

        val username = _username!!.text.toString()
        val firstName = _first_name!!.text.toString()
        val lastName = _last_name!!.text.toString()
        val email = _input_email!!.text.toString()
        val password = _input_password!!.text.toString()

        val breweryManager = BreweryManager(true, "")

        val user = UserWithPassword(username, Name(firstName, lastName), email, password, breweryManager)
        doAsync {
            Log.d("Signup Activity", "Making createUser request")
            try {
                val register_response = api.register(user).execute()
                if (register_response!!.isSuccessful) {
                    Log.d("Signup Activity", "Successful registration")
                    uiThread {
                        progressDialog.cancel()
                        _register_button!!.isEnabled = true
                        Toast.makeText(baseContext, "Welcome!", Toast.LENGTH_LONG).show()
                    }
                    val login_response = api.login(LoginUser(username, password)).execute()
                    if (login_response.isSuccessful) {
                        val token = login_response.body()
                        uiThread {
                            Log.d("Login", "Succesful")

                            val sharedPref = getSharedPreferences("BREWHAHA_PREF", Context.MODE_PRIVATE)
                            var editor = sharedPref.edit()
                            editor.putString("token", token!!.token)
                            editor.putString("refreshToken", token.refreshToken)
                            editor.commit()
                            //TODO: when the user logs out clear this

                            var bundle = bundleOf(
                                "token" to token!!.token,
                                "refreshToken" to token.refreshToken,
                                "id" to token.userId
                            )
                            val intent = Intent(baseContext, HomeActivity::class.java)
                            intent.putExtra("bundle", bundle)
                            startActivity(intent)
                        }
                    } else {
                        val error = when (login_response.code()) {
                            400 -> "Bad Request"
                            401 -> "Invalid credentials"
                            404 -> "Invalid Username"
                            409 -> "You are already logged in on another device"
                            500 -> "Something went wrong, try again!"
                            else -> "Make sure you are connected to the internet and try again!"

                        }
                        uiThread {
                            toast(error)
                            Log.d("Login Error", login_response.code().toString())
                        }
                    }
                } else {
                    val error = when (register_response.code()) {
                        400 -> "Bad Request"
                        401 -> "Invalid credentials"
                        404 -> "Resource not found"
                        409 -> "You are already logged in on another device"
                        500 -> "Something went wrong, try again!"
                        else -> "Make sure you are connected to the internet and try again!"

                    }
                    uiThread {
                        progressDialog.cancel()
                        onSignupFailed(error)
                    }
                }
            } catch (e: Exception) {
                Log.e("Signup Activity", e.message)
                Log.e("Signup Activity", e.getStackTraceString())
                progressDialog.cancel()
                onSignupFailed(e.message)
            } finally {
                Log.d("Signup Activity", "Executed")
            }
        }
    }

    fun onSignupFailed(error: String? = "") {
        Log.d("Signup Activity", "Signup Failed" + error)
        Toast.makeText(baseContext, "Signup Failed" + error, Toast.LENGTH_LONG).show()
        _register_button!!.isEnabled = true
    }

    fun validate(): Boolean {
        Log.d("Signup Activity", "Validate")
        var valid = true

        val usernameText = _username!!.text.toString()
        val firstName = _first_name!!.text.toString()
        val lastName = _last_name!!.text.toString()
        val email = _input_email!!.text.toString()
        val password = _input_password!!.text.toString()

        if (usernameText.isEmpty() || usernameText.length < 3) {
            _username!!.error = "Username must be at least 3 characters"
            valid = false
        } else {
            _username!!.error = null
        }

        if (firstName.isEmpty()) {
            _first_name!!.error = "Enter First Name"
            valid = false
        } else {
            _first_name!!.error = null
        }

        if (lastName.isEmpty()) {
            _last_name!!.error = "Enter Last Name"
            valid = false
        } else {
            _last_name!!.error = null
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _input_email!!.error = "Enter a valid email address"
            valid = false
        } else {
            _input_email!!.error = null
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            _input_password!!.error = "between 4 and 10 alphanumeric characters"
            valid = false
        } else {
            _input_password!!.error = null
        }

        return valid
    }
}
