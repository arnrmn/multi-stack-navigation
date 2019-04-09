package com.arnrmn.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arnrmn.nav.Navigator

class MainActivity : AppCompatActivity() {
    private val navigator: Navigator by lazy { Navigator(R.id.navigationContainer, supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigator.addGraph(R.navigation.second_nav)
        navigator.addGraph(R.navigation.first_nav)
    }

    override fun onBackPressed() {
        if (!navigator.onBackPressed()) super.onBackPressed()
    }
}
