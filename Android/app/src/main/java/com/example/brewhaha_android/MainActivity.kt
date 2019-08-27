package com.example.brewhaha_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showLogin(view: View): Unit{
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
