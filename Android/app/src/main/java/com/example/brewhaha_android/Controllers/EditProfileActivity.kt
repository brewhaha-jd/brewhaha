package com.example.brewhaha_android.Controllers

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.brewhaha_android.Api.BackendConnection
import com.example.brewhaha_android.R

class EditProfileActivity(private val api: BackendConnection = BackendConnection()) : AppCompatActivity() {

    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        bundle = intent.getBundleExtra("bundle")

        val userId = bundle!!["id"] as String
        val token = bundle!!["token"] as String


//        getUserDetails(userId, token) {
//            this.user = it
//            Log.d("Get User", "First Name: " + this.user!!.name.firstName)
//            Log.d("Get User", "Last Name: " + this.user!!.name.lastName)
//            Log.d("Get User", "Username: " + this.user!!.username)
//            Log.d("Get User", "Email: " + this.user!!.email)
//
//            firstNameEditText!!.setText(user!!.name.firstName)
//            lastNameEditText!!.setText(user!!.name.lastName)
//            userNameField!!.setText(user!!.username)
//            emailEditText!!.setText(user!!.email)
//        }

    }

//    fun getUserDetails(userId: String, token: String, callback: (User) -> Unit) {
//        // get user details here and pass back
//        doAsync {
//            val result = api.getUser(AuthToken(token, "", ""), userId).execute()
//            if (result.isSuccessful) {
//                Log.d("Get User", "User successfully retrieved from database")
//                uiThread{
//                    callback(result.body()!!)
//                }
//            } else {
//                Log.d("Get User", "Error Occured")
//                Log.d("Get User", "Error: " + result.errorBody()?.string())
//            }
//        }
//    }

    override fun onBackPressed() {
        super.onBackPressed()
        return
    }
}