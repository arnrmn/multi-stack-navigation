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

        firstContainer.findNavController().apply {
            graph = navInflater.inflate(R.navigation.nav_graph).apply {
                startDestination = R.id.one
            }
        }

        secondContainer.findNavController().apply {
            graph = navInflater.inflate(R.navigation.nav_graph).apply {
                startDestination = R.id.two
            }
        }

        navigationBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.firstContainer -> showFirst()
                R.id.secondContainer -> showSecond()
            }
            return@setOnNavigationItemSelectedListener true
        }

        showFirst()
    }

    private fun showFirst() {
        supportFragmentManager.beginTransaction()
            .hide(secondContainer)
            .show(firstContainer)
            .commit()
    }

    private fun showSecond() {
        supportFragmentManager.beginTransaction()
            .hide(firstContainer)
            .show(secondContainer)
            .commit()
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
