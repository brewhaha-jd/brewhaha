package com.example.brewhaha_android.Controllers

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.fonts.Font
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brewhaha_android.Api.BackendConnection
import com.example.brewhaha_android.Models.AuthToken
import com.example.brewhaha_android.Models.Brewery
import com.example.brewhaha_android.Models.LogoutUser
import com.example.brewhaha_android.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.jakewharton.rxbinding2.widget.textChanges
import info.androidhive.fontawesome.FontDrawable
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.filter_sheet.*
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.util.concurrent.TimeUnit

class HomeActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity() {

    var _mapView_button : MaterialButton? = null
    var _edit_brewery_button : MaterialButton? = null
    var _logout_button : FloatingActionButton? = null
    var _profile_button: FloatingActionButton? = null
    var _filter_button : MaterialButton? = null
    var _brewery_search : TextInputEditText? = null
    var _dummy_layout : LinearLayout? = null
    var tokenBundle: Bundle? = null
    lateinit var breweryList: ArrayList<Brewery>
    lateinit var oldFilteredBreweryList: ArrayList<Brewery>
    lateinit var filteredBrewerylist: ArrayList<Brewery>
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tokenBundle = intent.getBundleExtra("bundle")
        val userId = tokenBundle!!["id"] as String
        Log.d("Manager", "Home ID: " + userId)

        // Logout Button
        val logout_drawable = FontDrawable(this, R.string.fa_sign_out_alt_solid, true, false)
        logout_drawable.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        _logout_button = findViewById<FloatingActionButton>(R.id.logoutButton)
        _logout_button!!.setImageDrawable(logout_drawable)
        _logout_button!!.setOnClickListener{
            logout(userId)
        }

        // Profile Button
        val profile_drawable = FontDrawable(this, R.string.fa_user, true, false)
        profile_drawable.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        _profile_button = findViewById(R.id.profileButton)
        _profile_button!!.setImageDrawable(profile_drawable)
        _profile_button!!.setOnClickListener{
            goToEditProfilePage()
        }

        // Filter button
        _filter_button = findViewById<MaterialButton>(R.id.filterButton)
        _filter_button!!.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        _mapView_button = findViewById<MaterialButton>(R.id.mapView)
        _mapView_button!!.setOnClickListener{
            mapView(tokenBundle!!)
        }

        _edit_brewery_button = findViewById(R.id.editBreweryButton)
        isManager(userId) {
            Log.d("Manager", "Manager: " + it)
            if (!it) {
                _edit_brewery_button!!.visibility = View.GONE
            }
        }
        _edit_brewery_button!!.setOnClickListener {
            editBreweryView()
        }

        _brewery_search = findViewById<TextInputEditText>(R.id.brewerySearchBar)

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
        Log.d("Home", "Got Breweries")
        progressDialog.cancel()


        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = BreweryAdapter(oldFilteredBreweryList)
        }

        _brewery_search!!.textChanges()
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe{
                this
                    .search(it.toString())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{
                        val diffResult = DiffUtil.calculateDiff(
                            BreweryDiffUtilCallBack(
                                this.oldFilteredBreweryList, this.filteredBrewerylist))
                        this.oldFilteredBreweryList.clear()
                        this.oldFilteredBreweryList.addAll(this.filteredBrewerylist)
                        diffResult.dispatchUpdatesTo(recyclerView.adapter!!)
                        recyclerView.layoutManager!!.scrollToPosition(0)
                    }
            }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        _dummy_layout = findViewById<LinearLayout>(R.id.dummyLinearLayout)
        _brewery_search!!.clearFocus()
        _dummy_layout!!.requestFocus()
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

    fun goToEditProfilePage() {
        val intent = Intent(baseContext, EditProfileActivity::class.java)
        intent.putExtra("bundle", tokenBundle)
        startActivity(intent)
    }

    fun mapView(tokenBundle: Bundle) {
        val intent = Intent(baseContext, MapsActivity::class.java)
        intent.putExtra("bundle", tokenBundle)
        startActivity(intent)
    }

    fun isManager(userId: String, callback: (Boolean) -> Unit) {
        var token = AuthToken(tokenBundle!!["token"] as String, "", "")
        var ret = false
        doAsync {
            val response = api.getUser(token, userId).execute()
            if (response.isSuccessful) {
                val user = response.body()
                if (user.breweryManager.isManager) {
                    ret = true
                }
            } else {
                Log.d("Manager", "Error Code: " + response.code())
                Log.d("Manager", "Error Message: " + response.errorBody().string())
                ret = false
            }
            callback(ret)
        }
    }

    fun editBreweryView() {
        val intent = Intent(baseContext, EditBreweryActivity::class.java)
        intent.putExtra("bundle", tokenBundle)
        startActivity(intent)
    }

    private fun getBreweries() {
        val token = AuthToken(tokenBundle!!["token"] as String, "", "")
        Log.d("Home Brewery Call", "getting all breweries")
        doAsync {
            val response = api.getAllBreweries(token).execute()
            if (response.isSuccessful) {
                breweryList = ArrayList(response.body())
                oldFilteredBreweryList = ArrayList(response.body())
                filteredBrewerylist = ArrayList()
                uiThread {
                    Log.d("Home Brewery Call", "success")
                    Log.d("Home Brewery Call", "Size: " + breweryList.size)
                    breweryList.forEach {
                        Log.d("Home Brewery Call", "Name: " + it)
                    }
                }
            } else {
                uiThread {
                    Log.d("Home Brewery Call","Code: " + response.code())
                    Log.d("Home Brewery Call", "Error Message: " + response.errorBody())
                    Log.d("Home Brewery Call", "Response Body: " + response.message())
                }
            }
        }
    }

    fun search(query: String): Completable = Completable.create {
        if (query.length == 0) {
            filteredBrewerylist.clear()
            filteredBrewerylist.addAll(breweryList)
            it.onComplete()
        } else {
            var nameList = breweryList.map{ it.name }.toMutableList()
            val enumMap: HashMap<String, Int> = HashMap()
            breweryList.forEachIndexed{
                    i, element -> enumMap.put(element.name, i)
            }
            nameList = FuzzySearch.extractSorted(query, nameList, 5).map{
                Log.d("Home", it.toString())
                it.string
            }.toMutableList()
            val wanted = nameList.map {
                breweryList.get(enumMap[it]!!)
            }.toList()

            filteredBrewerylist.clear()
            filteredBrewerylist.addAll(wanted)
            it.onComplete()
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

class BreweryDiffUtilCallBack(private val oldList: List<Brewery>, private val newList: List<Brewery>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition]._id == newList[newItemPosition]._id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true // for the sake of simplicity we return true here but it can be changed to reflect a fine-grained control over which part of our views are updated
}