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
    var _view_all_breweries_button : MaterialButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_brewery)

        tokenBundle = intent.getBundleExtra("bundle")

        _view_all_breweries_button = findViewById(R.id.getAllBreweries)
        _go_back_home_button = findViewById(R.id.goBackToHome)

        _view_all_breweries_button!!.setOnClickListener{
            getBreweries()
        }

        _go_back_home_button!!.setOnClickListener {
            goHome()
        }
    }

    fun getBreweries() {
        val token = AuthToken(tokenBundle!!["token"] as String, "", "")
        Log.d("View Breweries", "getting all breweries")
        doAsync {
            val response = api.getAllBreweries(token).execute()
            if (response.isSuccessful) {
                val breweryList = response.body()
                uiThread {
                    Log.d("View Breweries", "success")
                    Log.d("View Breweries", "Size: " + breweryList.size)
                    breweryList.forEach{
                        Log.d("View Breweries", "Name: " + it)
                    }
                }
            } else {
                uiThread {
                    Log.d("View Breweries","Code: " + response.code())
                    Log.d("View Breweries", "Error Message: " + response.errorBody())
                    Log.d("View Breweries", "Response Body: " + response.message())
                }
            }
        }

    }

    fun goHome() {
        val intent = Intent(baseContext, HomeActivity::class.java)
        intent.putExtra("bundle", tokenBundle!!)
        startActivity(intent)
    }
}