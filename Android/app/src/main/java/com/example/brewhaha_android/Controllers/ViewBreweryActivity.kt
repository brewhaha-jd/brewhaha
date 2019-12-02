package com.example.brewhaha_android.Controllers

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.brewhaha_android.Api.BackendConnection
import com.example.brewhaha_android.Models.AuthToken
import com.example.brewhaha_android.Models.Brewery
import com.example.brewhaha_android.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.activity_view_brewery.*
import android.graphics.Point
import android.os.AsyncTask
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brewhaha_android.Models.FriendlinessRating
import com.example.brewhaha_android.Models.SubmitReviewModel
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.w3c.dom.Text
import java.lang.Exception
import java.time.LocalDateTime
import java.util.*


class ViewBreweryActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity() {

    var _kidsFoodRating: RatingBar? = null
    var _kidsEntertainmentRating: RatingBar? = null
    var _bathroomRatings: RatingBar? = null
    var _aggregateRating: RatingBar? = null
    var _agePicker: NumberPicker? = null
    var _reviewTextInput: TextInputEditText? = null
    var tokenBundle: Bundle? = null
    var brewery: Brewery? = null
    lateinit var reviewsList: List<SubmitReviewModel>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recycler_view: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.brewhaha_android.R.layout.activity_view_brewery)
        Log.d("BreweryView", "Created Brewery Activity")
        brewery = intent.getSerializableExtra("brewery") as? Brewery
        tokenBundle = intent.getBundleExtra("bundle")

//        linearLayoutManager = LinearLayoutManager(this)
//
//        getReviews()
//        while (!(::reviewsList.isInitialized)) {
//            Thread.sleep(1000)
//        }
//        recycler_view.apply {
//            layoutManager = linearLayoutManager
//            adapter = ReviewAdapter(reviewsList, tokenBundle!!)
//        }
//

        val _breweryName = findViewById<MaterialTextView>(R.id.detailedName)
        val _breweryImage = findViewById<ImageView>(R.id.detailedImage)
        val _breweryRating = findViewById<MaterialRatingBar>(R.id.aggregateRating)
        val _numRatings = findViewById<MaterialTextView>(R.id.numRatings)
        val _breweryAddress = findViewById<MaterialTextView>(R.id.detailedAddress)
        val _addReviewButton = findViewById<MaterialButton>(R.id.detailedAddRating)

        _breweryImage.setImageResource(R.drawable.beer_mugs)
        _breweryName.text = brewery!!.name
        _breweryAddress.text = String.format("%d %s, %s",
            brewery!!.address!!.number, brewery!!.address!!.line1, brewery!!.address!!.postalCode)
        val rating_double = brewery!!.friendlinessRating!!.aggregate
        if (rating_double == null) {
            _breweryRating.rating = 3f
            _numRatings?.text = "0 reviews"
        } else {
            _breweryRating?.rating = rating_double.toFloat()
            _breweryRating.isEnabled = false
            _numRatings?.text = "%d reviews".format(10)
        }
        _addReviewButton.setOnClickListener {
            showReviewPopup(brewery!!)
        }

        val _mapLocator = DownloadImageTask(findViewById<ImageView>(R.id.mapLocation))
        val lng = brewery!!.address!!.location.coordinates[0].toString()
        val lat = brewery!!.address!!.location.coordinates[1].toString()
        val API_KEY = "AIzaSyDmL2YGFYrTImNsXEV7RFP9iHNkY1Z-lS8"
        val center = "center=%s,%s".format(lat, lng)
        val zoomSize = "&zoom=15&size=300x300"
        val markers = "&markers=color:red%7Clabel:%7C" + lat + "," + lng
        val key = "&key=" + API_KEY
        val url = ("http://maps.google.com/maps/api/staticmap?" + center + zoomSize + markers + key)

        _mapLocator.execute(url)

    }

    private class DownloadImageTask(var bmImage: ImageView): AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg p0: String?): Bitmap {
            val url = p0[0]
            lateinit var icon: Bitmap
            try {
                val in_stream = java.net.URL(url).openStream()
                icon = BitmapFactory.decodeStream(in_stream)
            } catch (e: Exception) {
                Log.e("BreweryView", e.message)
                e.printStackTrace()
            }
            return icon
        }

        override fun onPostExecute(result: Bitmap) {
            bmImage.setImageBitmap(result)
        }
    }

    private fun getReviews() {
        val token = AuthToken(tokenBundle!!["token"] as String, "", "")
        Log.d("BreweryView getReviews", "getting all reviews")
        doAsync {
            val response = api.getReviews(token, brewery!!._id!!).execute()
            if (response.isSuccessful) {
                reviewsList = response.body()!!
                Log.d("BreweryView getReviews", reviewsList.toString())
                uiThread {
                    Log.d("BreweryView getReviews", "success")
                    Log.d("BreweryView getReviews", "Size: " + reviewsList.size)
                    reviewsList.forEach {
                        Log.d("BreweryView getReviews", "Name: " + it)
                    }
                }

            } else {
                uiThread {
                    Log.d("BreweryView getReviews", "Code: " + response.code())
                    Log.d("BreweryView getReviews", "Error Message: " + response.errorBody())
                    Log.d("BreweryView getReviews", "Response Body: " + response.message())
                }
            }
        }

    }

//    class ReviewAdapter(val reviewsList: List<SubmitReviewModel>, val tokenBundle: Bundle): RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
//
//        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            Log.d("BreweryView Adapter", reviewsList[position].brewery)
//            val brewery = reviewsList[position]
//            holder.image?.setImageResource(R.drawable.beer_mugs)
//            holder.name?.text = brewery.name
//            holder.address?.text = String.format("%d %s, %s", brewery.address!!.number,
//                brewery.address.line1, brewery.address.postalCode)
//            val rating_double = brewery.friendlinessRating!!.aggregate
//            if (rating_double == null) {
//                holder.rating?.rating = 3f
//                holder.numRatings?.text = "0 reviews"
//            } else {
//                holder.rating?.rating = rating_double.toFloat()
//                holder.numRatings?.text = "%d reviews".format(100)
//            }
//
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//            val v = LayoutInflater.from(parent.context).inflate(R.layout.brewery_card, parent, false)
//            return ViewHolder(v).listen { pos, type ->
//                val brewery = reviewsList.get(pos)
//                val context = v.context
//                val intent = Intent(context, ViewBreweryActivity::class.java)
//                intent.putExtra("brewery", brewery)
//                intent.putExtra("bundle", tokenBundle)
//                context.startActivity(intent)
//            }
//        }
//
//        fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
//            itemView.setOnClickListener {
//                event.invoke(getAdapterPosition(), getItemViewType())
//            }
//            return this
//        }
//
//        override fun getItemCount(): Int {
//            return breweryList.size
//        }
//
//        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//            val image = itemView.findViewById<ImageView>(R.id.breweryImage)
//            val name = itemView.findViewById<MaterialTextView>(R.id.breweryName)
//            val address = itemView.findViewById<MaterialTextView>(R.id.breweryAddress)
//            val numRatings = itemView.findViewById<MaterialTextView>(R.id.numRatings)
//            val rating = itemView.findViewById<MaterialRatingBar>(R.id.ratingBar)
//        }
//    }

    fun showReviewPopup(brewery: Brewery) {
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.rate_brewery_popup, null)

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y

        val popupWindow = PopupWindow(
            view,
            width-150,
            height-300
        )

        popupWindow.elevation = 40.0F
        popupWindow.isClippingEnabled = true
        popupWindow.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN


        // If API level 23 or higher then execute the code
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Create a new slide animation for popup window enter transition
            val slideIn = Slide()
            slideIn.slideEdge = Gravity.TOP
            popupWindow.enterTransition = slideIn

            // Slide animation for popup window exit transition
            val slideOut = Slide()
            slideOut.slideEdge = Gravity.BOTTOM
            popupWindow.exitTransition = slideOut

        }

        _reviewTextInput = view.findViewById<TextInputEditText>(R.id.reviewTextInput)
        _aggregateRating = view.findViewById<RatingBar>(R.id.aggregateRating)
        _kidsFoodRating = view.findViewById<RatingBar>(R.id.kidsFoodRating)
        _kidsEntertainmentRating = view.findViewById<RatingBar>(R.id.kidsEntertainmentRating)
        _bathroomRatings = view.findViewById<RatingBar>(R.id.bathroomsRating)
        _agePicker = view.findViewById<NumberPicker>(R.id.minAgePicker)

        _agePicker!!.minValue = 1
        _agePicker!!.maxValue = 21

        val submitReviewButton = view.findViewById<MaterialButton>(R.id.submitReviewButton)
        val closePopupButton = view.findViewById<MaterialButton>(R.id.closePopupButton)

        submitReviewButton.setOnClickListener {
            submitReview(brewery) {
                if (it) {
                    toast("Review successfully submitted")
                    popupWindow.dismiss()
                } else {
                    toast("Error submitting review, please try again")
                    popupWindow.dismiss()
                }
            }
        }

        closePopupButton.setOnClickListener {
            popupWindow.dismiss()
        }

        // Close the popup if clicked outside
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        TransitionManager.beginDelayedTransition(viewBreweryRootLayout)
        popupWindow.showAtLocation(
            viewBreweryRootLayout, // Location to display popup window
            Gravity.CENTER, // Exact position of layout to display popup
            0, // X offset
            -25 // Y offset
        )


    }

    fun submitReview(brewery: Brewery, callback: (Boolean) -> Unit) {
        if (_kidsFoodRating == null) {
            callback(false)
            return
        }
        val aggregateRating = _aggregateRating!!.rating
        val foodRating = _kidsFoodRating!!.rating
        val entertainmentRating = _kidsEntertainmentRating!!.rating
        val bathroomsRating = _bathroomRatings!!.rating
        val minAge = _agePicker!!.value
        val reviewText = _reviewTextInput!!.text
        val sharedPref = getSharedPreferences("BREWHAHA_PREF", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "NOT FOUND")
        val userId = sharedPref.getString("id", "NOT FOUND")
        Log.d("Submit Rating Token:", token)

        if (token == "NOT FOUND" || userId == "NOT FOUND") {
            toast("There was an error on our part, check back later")
            Thread.sleep(1000)
            callback(false)
            return
        }

        val authToken = AuthToken(token!!, "", "")

        val friendlinessRating = FriendlinessRating(
            aggregateRating.toDouble(),
            foodRating.toDouble(),
            entertainmentRating.toDouble(),
            bathroomsRating.toDouble(),
            minAge.toDouble()
        )

        val submitReview = SubmitReviewModel(
            userId!!,
            brewery._id!!,
            Calendar.getInstance().time,
            friendlinessRating,
            reviewText.toString()
        )

        doAsync {
            val result = api.submitReview(authToken, submitReview).execute()
            if (result.isSuccessful) {
                Log.d("Review", "Success")
                val map = result.body()!!
                Log.d("Review", map["reviewLink"])
                uiThread{
                    callback(true)
                }

            } else {
                Log.d("Error", result.message())
                uiThread{
                    callback(false)
                }
            }
        }

    }
}