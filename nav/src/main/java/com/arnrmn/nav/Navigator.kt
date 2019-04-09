package com.arnrmn.nav

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment

class Navigator(
    @IdRes private val containerViewId: Int,
    private val fragmentManager: FragmentManager
) {

    //todo add multiple graphs at one go
    fun addGraph(@NavigationRes graphId: Int, startDestinationArgs: Bundle? = null) {
        val host = NavHostFragment.create(graphId, startDestinationArgs)
        fragmentManager.beginTransaction()
            .add(containerViewId, host, graphId.toString())
            .also { transaction -> getVisible()?.let { host -> transaction.hide(host) } }
            .commit()
    }

    /**
     * Shows navigation host fragment with provided graph id if such exists.
     * */
    fun show(@NavigationRes graphId: Int) {
        val visible = getVisible()
        val target = getHost(graphId)
        if (target != null && visible != target) {
            fragmentManager.beginTransaction()
                .show(target)
                .also { transaction -> if (visible != null) transaction.hide(visible) }
                .commit()
        }
    }

    /**
     * Returns true when consumes back button click, false otherwise.
     */
    fun onBackPressed(): Boolean =
        getVisible()?.navController?.navigateUp() ?: false

    private fun getHost(@NavigationRes graphId: Int): NavHostFragment? =
        fragmentManager.findFragmentByTag(graphId.toString()) as? NavHostFragment

    private fun getVisible(): NavHostFragment? =
        fragmentManager.fragments
            .filterIsInstance<NavHostFragment>()
            .firstOrNull(NavHostFragment::isVisible)
}