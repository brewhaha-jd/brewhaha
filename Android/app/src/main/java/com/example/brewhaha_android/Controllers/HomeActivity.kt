package com.example.brewhaha_android.Controllers

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.brewhaha_android.R

class HomeActivity : AppCompatActivity() {
    var _token_text: TextView? = null
    var _refreshToken_text : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        _token_text = findViewById<TextView>(R.id.tokenTextView)
        _refreshToken_text = findViewById<TextView>(R.id.refreshTokenTextView)

        var tokenBundle = intent.getBundleExtra("bundle")
        var token = tokenBundle["token"]
        var refreshToken = tokenBundle["refreshToken"]

        _token_text!!.text = token.toString()
        _refreshToken_text!!.text = refreshToken.toString()

    }
}