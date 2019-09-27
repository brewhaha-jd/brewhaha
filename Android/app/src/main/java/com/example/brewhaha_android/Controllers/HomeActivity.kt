package com.example.brewhaha_android.Controllers

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brewhaha_android.Api.BackendConnection
import com.example.brewhaha_android.Models.AuthToken
import com.example.brewhaha_android.Models.Brewery
import com.example.brewhaha_android.Models.LogoutUser
import com.example.brewhaha_android.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class HomeActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity() {
    var _logout_button : MaterialButton? = null
    var _filter_button : MaterialButton? = null
    var tokenBundle: Bundle? = null
    lateinit var breweryList: List<Brewery>
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Logout stuff
        _logout_button = findViewById<MaterialButton>(R.id.logoutButton)
        tokenBundle = intent.getBundleExtra("bundle")
        val userId = tokenBundle!!["id"] as String
        _logout_button!!.setOnClickListener{
            logout(userId)
        }

        _filter_button = findViewById<MaterialButton>(R.id.filterButton)
        _filter_button!!.setOnClickListener {
        // TODO: make filter sheet
        }

        val progressDialog = ProgressDialog(this)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Finding Breweries...")
        progressDialog.show()

        // Get brewery stuff
        linearLayoutManager = LinearLayoutManager(this)
        Log.d("Home", "About to get breweries")
        Log.d("Home", "GetBreweries = ")
        getBreweries()
        while (!(::breweryList.isInitialized)) {
            Thread.sleep(1000)

        }
        progressDialog.cancel()

        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = BreweryAdapter(breweryList)
//            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }

    fun logout(userId: String) {
        val logoutUser = LogoutUser(userId)
        Log.d("Home Logout", "Logging out: " + userId)
        doAsync {
            val response = api.logout(logoutUser).execute()
            if (response.isSuccessful) {
                uiThread {
                    toast("Logged Out")
                    val intent = Intent(baseContext, LoginActivity::class.java)
                    startActivity(intent)
                }
            } else {
                val error = when (response.code()) {
                    400 -> "Bad Request"
                    401 -> "Invalid credentials"
                    404 -> "Resource not found"
                    409 -> "You are already logged in on another device"
                    500 -> "Something went wrong, try again!"
                    else -> "Make sure you are connected to the internet and try again!"

                }
                uiThread {
                    toast(error)
                    Log.d("Login Error", response.code().toString())
                }
            }
        }
    }

    fun getBreweries() {
        val token = AuthToken(tokenBundle!!["token"] as String, "", "")
        Log.d("Home Brewery Call", "getting all breweries")
        doAsync {
            val response = api.getAllBreweries(token).execute()
            if (response.isSuccessful) {
                breweryList = response.body()
                Log.d("Home Brewery Call", breweryList.toString())
                uiThread {
                    Log.d("Home Brewery Call", "success")
                    Log.d("Home Brewery Call", "Size: " + breweryList.size)
                    breweryList.forEach {
                        Log.d("Home Brewery Call", "Name: " + it)
                    }
                }

            }
            else {
                uiThread {
                    Log.d("Home Brewery Call","Code: " + response.code())
                    Log.d("Home Brewery Call", "Error Message: " + response.errorBody())
                    Log.d("Home Brewery Call", "Response Body: " + response.message())
                }
            }
        }
    }
}

class BreweryAdapter(val breweryList: List<Brewery>): RecyclerView.Adapter<BreweryAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("Home Adapter", breweryList[position].name)
        val brewery = breweryList[position]
        holder.image?.setImageResource(R.drawable.beer_mugs)
        holder.name?.text = brewery.name
        holder.address?.text = String.format("%d %s, %s", brewery.address!!.number,
            brewery.address.line1, brewery.address.postalCode)
        val rating_double = brewery.friendlinessRating!!.aggregate
        if (rating_double == null) {
            holder.rating?.text = "Rating coming soon!"
        } else {
            holder.rating?.text = String.format("Rating: %d", rating_double.toString())
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.brewery_card, parent, false)
        return ViewHolder(v)
    }


    override fun getItemCount(): Int {
        return breweryList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val image = itemView.findViewById<ImageView>(R.id.breweryImage)

        val name = itemView.findViewById<MaterialTextView>(R.id.breweryName)
        val address = itemView.findViewById<MaterialTextView>(R.id.breweryAddress)
        val rating = itemView.findViewById<MaterialTextView>(R.id.breweryRating)

    }
}