package com.example.brewhaha_android.Controllers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.brewhaha_android.R
import android.widget.Button
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    var _register_button: Button? = null
    var _login_button: Button? = null
    var _email_text: EditText? = null
    var _password_text: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        _register_button = findViewById<MaterialButton>(R.id.registerButton)
        _login_button = findViewById<MaterialButton>(R.id.loginButton)
        _email_text = findViewById<TextInputEditText>(R.id.emailText)
        _password_text = findViewById<TextInputEditText>(R.id.passwordText)
//        _login_button.setOnClickListener()

        _register_button!!.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}