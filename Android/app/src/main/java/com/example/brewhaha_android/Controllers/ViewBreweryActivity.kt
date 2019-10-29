package com.example.brewhaha_android.Controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.brewhaha_android.Api.BackendConnection
import com.example.brewhaha_android.Models.AuthToken
import com.example.brewhaha_android.Models.Brewery
import com.example.brewhaha_android.R
import com.google.android.material.button.MaterialButton
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ViewBreweryActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity() {
    var tokenBundle: Bundle? = null
    var _go_back_home_button : MaterialButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_brewery)
        Log.d("BREWERYVIEW", "WE CREATED BREWERY ACTIVITY")

    }
}