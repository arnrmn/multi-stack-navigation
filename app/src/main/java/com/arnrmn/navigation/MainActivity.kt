package com.arnrmn.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        multiNavContainer.addBranch(R.navigation.first_nav)
        multiNavContainer.addBranch(R.navigation.second_nav)
    }

    override fun onBackPressed() {
        if (getNavController()?.navigateUp() == false) super.onBackPressed()
    }

    private fun getNavController(): NavController? {
        return supportFragmentManager
            .fragments
            .filter { fragment -> fragment is NavHostFragment }
            .firstOrNull(Fragment::isVisible)
            ?.findNavController()
    }
}
