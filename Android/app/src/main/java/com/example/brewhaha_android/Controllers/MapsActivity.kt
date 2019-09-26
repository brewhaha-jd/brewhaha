package com.example.brewhaha_android.Controllers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.brewhaha_android.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,  GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
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
        val myLocation = LatLng(20.5937, 78.9629)
        mMap.addMarker(MarkerOptions().position(myLocation).title("Marker in India"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
        mMap.uiSettings.isZoomControlsEnabled = true
    }
    override fun onMarkerClick(p0: Marker?) = false

}