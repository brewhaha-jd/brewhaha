package com.example.brewhaha_android.Controllers

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.brewhaha_android.Api.BackendConnection
import com.example.brewhaha_android.Models.Brewery
import com.example.brewhaha_android.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class EditBreweryActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity() {

    private var bundle: Bundle? = null


    var _breweryName: TextInputEditText? = null
    var _addressLine1: TextInputEditText? = null
    var _addressLine2: TextInputEditText? = null
    var _city: TextInputEditText? = null
    var _state: TextInputEditText? = null
    var _zipCode: TextInputEditText? = null
    var _phoneNumber: TextInputEditText? = null
    var _website: TextInputEditText? = null

    var _update_button: MaterialButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_brewery)


        Log.d("EditBrewery", "Created Edit Brewery Activity")


        val brewery = intent.getSerializableExtra("brewery") as? Brewery

        _breweryName = findViewById<TextInputEditText>(R.id.breweryName)
        _addressLine1 = findViewById<TextInputEditText>(R.id.addressLine1)
        _addressLine2 = findViewById<TextInputEditText>(R.id.addressLine2)
        _city = findViewById<TextInputEditText>(R.id.city)
        _state = findViewById<TextInputEditText>(R.id.state)
        _zipCode = findViewById<TextInputEditText>(R.id.zipCode)
        _phoneNumber = findViewById<TextInputEditText>(R.id.phoneNumber)
        _website = findViewById<TextInputEditText>(R.id.website)

        _update_button = findViewById<MaterialButton>(R.id.updateButton)


        _breweryName!!.setText(brewery!!.name)
        _addressLine1!!.setText(String.format("%d %s", brewery.address!!.number, brewery.address.line1))
        _addressLine2!!.setText(brewery.address.line2)
        _city!!.setText(brewery.address.line3)
        _state!!.setText(brewery.address.stateOrProvince)
        _zipCode!!.setText(brewery.address.postalCode)
        _phoneNumber!!.setText(brewery.address.telephone)
        _website!!.setText(brewery.website)


        _update_button!!.setOnClickListener{ updateBrewery() }

    }

    private fun updateBrewery() {
        Log.d("EditBrewery", "Update Brewery Activity")

        _update_button!!.isEnabled = false

        val progressDialog = ProgressDialog(this)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Updating Brewery Information...")
        progressDialog.show()

        //BELOW SHOULD BE EVERYTHING NEEDED FOR UPDATE BREWERY API
        //EXCEPT FOR LAT, LNG
        //TODO: VALIDATE INPUTS
        //TODO: RESERVE ADDRESS LOOKUP (address to LAT, LNG)

        val breweryName = _breweryName?.text.toString()
        val address = _addressLine1?.text.toString()
        val stNumber = address?.substringBefore(" ", "")
        val address1 = address?.substringAfter(" ", "")
        val address2 = _addressLine2?.text.toString()
        val address3 = _city?.text.toString()
        val state = _state?.text.toString()
        val zipCode = _zipCode?.text.toString()
        val phone = _phoneNumber?.text.toString()
        val website = _website?.text .toString()


        //TODO: HOOKUP API


        progressDialog.cancel()


    }

    override fun onBackPressed() {
        super.onBackPressed()
        return
    }
}

