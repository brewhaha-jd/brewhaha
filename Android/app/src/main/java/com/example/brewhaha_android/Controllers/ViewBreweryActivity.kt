package com.example.brewhaha_android.Controllers

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
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
import android.view.WindowManager
import android.widget.*
import com.example.brewhaha_android.Models.FriendlinessRating
import com.example.brewhaha_android.Models.SubmitReviewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.w3c.dom.Text
import java.time.LocalDateTime
import java.util.*


class ViewBreweryActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity() {

    var _kidsFoodRating: RatingBar? = null
    var _kidsEntertainmentRating: RatingBar? = null
    var _bathroomRatings: RatingBar? = null
    var _aggregateRating: RatingBar? = null
    var _agePicker: NumberPicker? = null
    var _reviewTextInput: TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.brewhaha_android.R.layout.activity_view_brewery)
        Log.d("BreweryView", "Created Brewery Activity")
        val brewery = intent.getSerializableExtra("brewery") as? Brewery

        val _breweryName = findViewById<MaterialTextView>(com.example.brewhaha_android.R.id.detailedName)
        val _breweryImage = findViewById<ImageView>(com.example.brewhaha_android.R.id.detailedImage)
        val _breweryRating = findViewById<MaterialTextView>(com.example.brewhaha_android.R.id.detailedRating)
        val _breweryAddress = findViewById<MaterialTextView>(com.example.brewhaha_android.R.id.detailedAddress)
        val _addReviewButton = findViewById<MaterialButton>(com.example.brewhaha_android.R.id.detailedAddRating)

        _breweryImage.setImageResource(com.example.brewhaha_android.R.drawable.beer_mugs)
        _breweryName.text = brewery!!.name
        _breweryAddress.text = String.format("%d %s, %s", brewery.address!!.number,
            brewery.address.line1, brewery.address.postalCode)
        val rating_double = brewery.friendlinessRating!!.aggregate
        if (rating_double == null) {
            _breweryRating.text = "Rating coming soon!"
        } else {
            _breweryRating.text = String.format("Rating: %d", rating_double.toString())
        }

        _addReviewButton.setOnClickListener {
            showReviewPopup(brewery)
        }

    }

    fun showReviewPopup(brewery: Brewery) {
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(com.example.brewhaha_android.R.layout.rate_brewery_popup, null)

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
                } else {
                    toast("Error submitting review, please try again")
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

        val friendlinessRating = FriendlinessRating(
            aggregateRating.toDouble(),
            foodRating.toDouble(),
            entertainmentRating.toDouble(),
            bathroomsRating.toDouble(),
            minAge
        )

        val submitReview = SubmitReviewModel(
            0,
            brewery._id!!.toInt(),
            Calendar.getInstance().time,
            friendlinessRating,
            reviewText.toString()
        )

        doAsync {

        }

    }
}