package com.example.mybookshelf

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_bottom_nav, container, false)
        val nav = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Check the current activity
        val currentActivity = activity?.javaClass?.simpleName
        // Highlight the current tab
        when (currentActivity) {
            ListActivity::class.java.simpleName -> nav.selectedItemId = R.id.list
            TbrActivity::class.java.simpleName -> nav.selectedItemId = R.id.tbr
            ReadActivity::class.java.simpleName -> nav.selectedItemId = R.id.read
        }

        // Handle nav item clicks
        nav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.list -> {
                    Log.d("AppLogs", "Navigating to ListActivity")
                    startActivity(Intent(requireContext(), ListActivity::class.java))
                }
                R.id.tbr -> {
                    Log.d("AppLogs", "Navigating to TbrActivity")
                    startActivity(Intent(requireContext(), TbrActivity::class.java))
                }
                R.id.read -> {
                    Log.d("AppLogs", "Navigating to ReadActivity")
                    startActivity(Intent(requireContext(), ReadActivity::class.java))
                }
            }
            true
        }
        return view
    }
}
