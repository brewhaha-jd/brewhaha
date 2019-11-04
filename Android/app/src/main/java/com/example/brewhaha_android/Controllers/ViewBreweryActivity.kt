package com.example.brewhaha_android.Controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import com.example.brewhaha_android.Api.BackendConnection
import com.example.brewhaha_android.Models.AuthToken
import com.example.brewhaha_android.Models.Brewery
import com.example.brewhaha_android.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.Serializable

class ViewBreweryActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_brewery)
        Log.d("BreweryView", "Created Brewery Activity")
        val brewery = intent.getSerializableExtra("brewery") as? Brewery
        val _breweryName = findViewById<MaterialTextView>(R.id.detailedName)
        val _breweryImage = findViewById<ImageView>(R.id.detailedImage)
        val _breweryRating = findViewById<MaterialTextView>(R.id.detailedRating)
        val _breweryAddress = findViewById<MaterialTextView>(R.id.detailedAddress)

        _breweryImage.setImageResource(R.drawable.beer_mugs)
        _breweryName.text = brewery!!.name
        _breweryAddress.text = String.format("%d %s, %s", brewery.address!!.number,
            brewery.address.line1, brewery.address.postalCode)
        val rating_double = brewery.friendlinessRating!!.aggregate
        if (rating_double == null) {
            _breweryRating.text = "Rating coming soon!"
        } else {
            _breweryRating.text = String.format("Rating: %d", rating_double.toString())
        }

    }
}