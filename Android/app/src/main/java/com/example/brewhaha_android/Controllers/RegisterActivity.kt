package com.example.brewhaha_android.Controllers

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.brewhaha_android.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    var _register_button: Button? = null
    var _input_email: EditText? = null
    var _input_password: EditText? = null
    var _username: EditText? = null
    var _first_name: EditText? = null
    var _last_name: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        _register_button = findViewById(R.id.registerButton) as MaterialButton
        _input_email = findViewById(R.id.email) as TextInputEditText
        _input_password = findViewById(R.id.password) as TextInputEditText
        _username = findViewById(R.id.username) as TextInputEditText
        _first_name = findViewById(R.id.firstName) as TextInputEditText
        _last_name = findViewById(R.id.lastName) as TextInputEditText
        // TODO: set registerButton.setOnClickListener()

    }
}