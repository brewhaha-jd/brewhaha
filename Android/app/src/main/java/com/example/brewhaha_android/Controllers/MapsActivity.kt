package com.example.brewhaha_android.Controllers

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MapsActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity(), OnMapReadyCallback,  GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    lateinit var breweryList: List<Brewery>
    var tokenBundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        tokenBundle = intent.getBundleExtra("bundle")
        val userId = tokenBundle!!["id"] as String

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
    }


    override fun onMarkerClick(p0: Marker?) = false


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