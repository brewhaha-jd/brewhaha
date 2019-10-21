package com.example.brewhaha_android.Controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.brewhaha_android.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup

class BottomSheetFragment : BottomSheetDialogFragment() {
    var _distance_button_group: MaterialButtonToggleGroup? = null
    var _rating_button_group: MaterialButtonToggleGroup? = null
    var _done_button: MaterialButton? = null
    var distance_selected  = ""
    var rating_selected  = ""
    private lateinit var viewOfLayout: View

    companion object {
        fun newInstance(): BottomSheetFragment {
            return BottomSheetFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater.inflate(R.layout.filter_sheet, container, false)

        _distance_button_group = viewOfLayout.findViewById(R.id.distance_group)
        _rating_button_group = viewOfLayout.findViewById(R.id.rating_group)
        _done_button = viewOfLayout.findViewById(R.id.filters_done_button)

        _distance_button_group!!.setSingleSelection(true)
        _rating_button_group!!.setSingleSelection(true)

        _distance_button_group!!.addOnButtonCheckedListener{ group, checkedId, isChecked ->
            if (isChecked) {
                distance_selected = view!!.findViewById<MaterialButton>(checkedId).text.toString()
            }
            Log.d("Filters", "Distance Selected: " + distance_selected)
        }

        _rating_button_group!!.addOnButtonCheckedListener{ group, checkedId, isChecked ->
            if (isChecked) {
                rating_selected = view!!.findViewById<MaterialButton>(checkedId).text.toString()
            }
            Log.d("Filters", "Rating Selected: " + rating_selected)
        }

        _done_button!!.setOnClickListener {
            createBundleAndDismissFragment()
        }
        return viewOfLayout
    }

    fun createBundleAndDismissFragment() {
        var activity: HomeActivity = activity!! as HomeActivity

        val distanceMap: HashMap<String, String> = HashMap()
        distanceMap.put("", "")
        distanceMap.put("< 5 mi", "5")
        distanceMap.put("< 10 mi", "10")
        distanceMap.put("< 15 mi", "15")
        distanceMap.put("15+ mi", "50")

        val ratingMap: HashMap<String, String> = HashMap()
        ratingMap.put("", "")
        ratingMap.put("2+", "2")
        ratingMap.put("3+", "3")
        ratingMap.put("4+", "4")

        activity._distance_filter = distanceMap[distance_selected]!!
        activity._rating_filter = ratingMap[rating_selected]!!

        val fragment = activity.supportFragmentManager.findFragmentByTag("BOTTOM SHEET FRAGMENT")
        Log.d("Filters", "Fragment Is Null: " + (fragment != null))
        activity.customOnResume()
        if (fragment != null) {
            Log.d("Filters", "Removing The Fragment")
            activity.supportFragmentManager.beginTransaction().detach(fragment).commit()
        }
    }
}