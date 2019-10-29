package com.example.brewhaha_android.Controllers

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.brewhaha_android.Api.BackendConnection
import com.example.brewhaha_android.Models.AuthToken
import com.example.brewhaha_android.Models.Brewery
import com.example.brewhaha_android.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MapsActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity(), OnMapReadyCallback,  GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    lateinit var breweryList: List<Brewery>
    lateinit var curr_brewery: Brewery
    var tokenBundle: Bundle? = null
    var _listViewButton : MaterialButton? = null
    var _card_image : ImageView? = null
    var _card_name : MaterialTextView? = null
    var _card_address : MaterialTextView? = null
    var _card_rating : MaterialTextView? = null
    var _map_card : CardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        tokenBundle = intent.getBundleExtra("bundle")
        val userId = tokenBundle!!["id"] as String

        _listViewButton = findViewById<MaterialButton>(R.id.listView)
        _listViewButton!!.setOnClickListener {
            finish()
        }

        val progressDialog = ProgressDialog(this)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Finding Breweries...")
        progressDialog.show()

        Log.d("Maps", "About to get breweries")
        Log.d("Maps", "GetBreweries = ")

        getBreweries()

        while (!(::breweryList.isInitialized)) {
            Thread.sleep(1000)
        }

        progressDialog.cancel()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        _card_image = findViewById<ImageView>(R.id.breweryImage)
        _card_name = findViewById<MaterialTextView>(R.id.breweryName)
        _card_address = findViewById<MaterialTextView>(R.id.breweryAddress)
        _card_rating = findViewById<MaterialTextView>(R.id.breweryRating)
        curr_brewery = breweryList[0]
        updateCard()

        _map_card = findViewById<CardView>(R.id.mapCard)
        _map_card!!.setOnClickListener {
            val intent = Intent(baseContext, ViewBreweryActivity::class.java)
            intent.putExtra("brewery", curr_brewery)
            startActivity(intent)
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        // Add a marker in Atlanta and move the camera
        val myLocation = LatLng(33.7490, -84.3880)
        //mMap.addMarker(MarkerOptions().position(myLocation).title("Marker in Atlanta"))
        breweryList.forEach {
            val lng = it.address!!.location.coordinates[0]
            val lat = it.address!!.location.coordinates[1]
            val point = LatLng(lat.toDouble(), lng.toDouble())
            mMap.addMarker(MarkerOptions().position(point).title(it.name))
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        if (p0 != null) {
            val name = p0.title
            val clicked_brewery = breweryList.find { it.name == name }
            if (clicked_brewery != null) {
                curr_brewery = clicked_brewery
                updateCard()
            }
        }
        return false
    }

    private fun updateCard() {
        _card_image?.setImageResource(R.drawable.beer_mugs)
        _card_name?.text = curr_brewery.name
        _card_address?.text = String.format("%d %s, %s", curr_brewery.address!!.number,
            curr_brewery.address!!.line1, curr_brewery.address!!.postalCode)
        val rating_double = curr_brewery.friendlinessRating!!.aggregate
        if (rating_double == null) {
            _card_rating?.text = "Rating coming soon!"
        } else {
            _card_rating?.text = String.format("Rating: %d", rating_double.toString())
        }
    }

    private fun getBreweries() {
        val token = AuthToken(tokenBundle!!["token"] as String, "", "")
        Log.d("Maps Brewery Call", "getting all breweries")
        doAsync {
            val response = api.getAllBreweries(token).execute()
            if (response.isSuccessful) {
                breweryList = response.body()
                Log.d("Maps Brewery Call", breweryList.toString())
                uiThread {
                    Log.d("Maps Brewery Call", "success")
                    Log.d("Maps Brewery Call", "Size: " + breweryList.size)
                    breweryList.forEach {
                        Log.d("Maps Brewery Call", "Name: " + it)
                    }
                }

            } else {
                uiThread {
                    Log.d("Maps Brewery Call", "Code: " + response.code())
                    Log.d("Maps Brewery Call", "Error Message: " + response.errorBody())
                    Log.d("Maps Brewery Call", "Response Body: " + response.message())
                }
            }
        }

    }
}