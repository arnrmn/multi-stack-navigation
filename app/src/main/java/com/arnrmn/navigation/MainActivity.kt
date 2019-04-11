package com.arnrmn.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arnrmn.nav.Graph
import com.arnrmn.nav.Navigator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val navigator: Navigator by lazy { Navigator(R.id.navigationContainer, supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigator.initNavigation(
            Graph(R.navigation.first_nav),
            Graph(R.navigation.second_nav)
        )

        navigator.addTopGraphChangeListener { graphId -> navigationBar.selectedItemId = graphId }
        navigationBar.setOnNavigationItemSelectedListener { item -> navigator.show(item.itemId) }
    }

    override fun onBackPressed() {
        if (!navigator.navigateUp()) super.onBackPressed()
    }
}
