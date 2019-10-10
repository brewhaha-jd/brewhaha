package com.example.brewhaha_android.Controllers

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.brewhaha_android.Api.BackendConnection
import com.example.brewhaha_android.R

class EditBreweryActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity() {

    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_brewery)

        bundle = intent.getBundleExtra("bundle")

    }

    override fun onBackPressed() {
        super.onBackPressed()
        return
    }
}