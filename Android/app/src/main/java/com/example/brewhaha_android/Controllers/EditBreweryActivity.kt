package com.example.brewhaha_android.Controllers

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.brewhaha_android.Api.BackendConnection
import com.example.brewhaha_android.Models.AddBrewery
import com.example.brewhaha_android.Models.Address
import com.example.brewhaha_android.Models.AuthToken
import com.example.brewhaha_android.Models.Brewery
import com.example.brewhaha_android.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.getStackTraceString
import org.jetbrains.anko.uiThread
import java.lang.Exception

class EditBreweryActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity() {

    private var tokenBundle: Bundle? = null


    var _breweryName: TextInputEditText? = null
    var _addressLine1: TextInputEditText? = null
    var _addressLine2: TextInputEditText? = null
    var _city: TextInputEditText? = null
    var _state: TextInputEditText? = null
    var _zipCode: TextInputEditText? = null
    var _phoneNumber: TextInputEditText? = null
    var _website: TextInputEditText? = null

    var _update_button: MaterialButton? = null

    lateinit var brewery: Brewery


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_brewery)


        Log.d("EditBrewery", "Created Edit Brewery Activity")

        tokenBundle = intent.getBundleExtra("bundle")
        brewery = intent.getSerializableExtra("brewery") as Brewery


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
        _addressLine1!!.setText(String.format("%d %s", brewery.address!!.number, brewery.address!!.line1))
        _addressLine2!!.setText(brewery.address!!.line2)
        _city!!.setText(brewery.address!!.line3)
        _state!!.setText(brewery.address!!.stateOrProvince)
        _zipCode!!.setText(brewery.address!!.postalCode)
        _phoneNumber!!.setText(brewery.address!!.telephone)
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

        var token = AuthToken(tokenBundle!!["token"] as String, "", "")

        //BELOW SHOULD BE EVERYTHING NEEDED FOR UPDATE BREWERY API
        //EXCEPT FOR LAT, LNG
        //TODO: VALIDATE INPUTS
        //TODO: REVERSE ADDRESS LOOKUP (address to LAT, LNG)

        val breweryName = _breweryName?.text.toString()
        val address = _addressLine1?.text.toString()
        val stNumber = address?.substringBefore(" ", "").toInt()
        val address1 = address?.substringAfter(" ", "")
        val address2 = _addressLine2?.text.toString()
        val city = _city?.text.toString()
        val state = _state?.text.toString()
        val zipCode = _zipCode?.text.toString()
        val phone = _phoneNumber?.text.toString()
        val website = _website?.text .toString()

        val location = brewery!!.address!!.location

        

        val updatedBrewery = AddBrewery(Address(brewery.address!!.location, stNumber, address1, address2, brewery.address!!.line3, city, state, brewery.address!!.county, brewery.address!!.country, zipCode, phone), breweryName, website)

        doAsync {
            Log.d("EditBrewery", "Making update brewery request")
            try {
                val update_response = api.updateBrewery(token, brewery._id!!, updatedBrewery).execute()
                if (update_response!!.isSuccessful) {
                    Log.d("Signup Activity", "Successful registration")
                    uiThread {
                        progressDialog.cancel()
                        _update_button!!.isEnabled = true
                        Toast.makeText(baseContext, "Welcome!", Toast.LENGTH_LONG).show()
                    }
                }

                val intent = Intent(baseContext, ViewBreweryActivity::class.java)
                intent.putExtra("bundle", tokenBundle)
                intent.putExtra("brewery", update_response.body())
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("EditBrewery", e.message)
                Log.e("EditBrewery", e.getStackTraceString())
                progressDialog.cancel()
            } finally {
                Log.d("EditBrewery", "Executed")
            }
        }



        progressDialog.cancel()


    }

    override fun onBackPressed() {
        super.onBackPressed()
        return
    }
}

