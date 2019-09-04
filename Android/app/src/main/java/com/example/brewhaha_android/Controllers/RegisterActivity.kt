package com.example.brewhaha_android.Controllers

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.brewhaha_android.Api.BackendConnection
import com.example.brewhaha_android.Models.Name
import com.example.brewhaha_android.Models.UserWithPassword
import com.example.brewhaha_android.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    var _register_button: MaterialButton? = null
    var _input_email: TextInputEditText? = null
    var _input_password: TextInputEditText? = null
    var _username: TextInputEditText? = null
    var _first_name: TextInputEditText? = null
    var _last_name: TextInputEditText? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        _input_email = findViewById<TextInputEditText>(R.id.email)
        _input_password = findViewById<TextInputEditText>(R.id.password)
        _username = findViewById<TextInputEditText>(R.id.username)
        _first_name = findViewById<TextInputEditText>(R.id.firstName)
        _last_name = findViewById<TextInputEditText>(R.id.lastName)
        _register_button = findViewById<MaterialButton>(R.id.registerButton)
        _register_button!!.setOnClickListener{ createUser() }

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

        val user = UserWithPassword(username, Name(firstName, lastName), email, password)
        Log.d("Signup Activity", "Created a user")
        val response = BackendConnection().register(user).execute()
        if (response.isSuccessful) {
            onSignupSuccess()
        } else {
            onSignupFailed()
            Log.d("Signup Activity", response.message())
        }
    }


    fun onSignupSuccess() {
        _register_button!!.isEnabled = true
        Toast.makeText(baseContext, "Welcome!", Toast.LENGTH_LONG).show()
    }

    fun onSignupFailed() {
        Toast.makeText(baseContext, "Signup Failed", Toast.LENGTH_LONG).show()

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