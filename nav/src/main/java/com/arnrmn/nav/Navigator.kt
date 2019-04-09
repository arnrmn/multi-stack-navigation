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
    fun addGraph(@NavigationRes graphId: Int, startDestinationArgs: Bundle? = null) {
        val host = NavHostFragment.create(graphId, startDestinationArgs)
        fragmentManager.beginTransaction()
            .add(containerViewId, host, graphId.toString())
            .show(host)
            .also { transaction -> getVisible()?.let { host -> transaction.hide(host) } }
            .commit()
    }

    fun swichTo(@NavigationRes graphId: Int) {
        val visible = getVisible()
        val target = getHost(graphId)
        if (target != null && visible != target) {
            fragmentManager.beginTransaction()
                .show(target)
                .also { transaction -> if (visible != null) transaction.hide(visible) }
                .commit()
        }
    }

    fun navigateUp(): Boolean {
        return getVisible()?.navController?.navigateUp() ?: false
    }

    private fun getHost(@NavigationRes graphId: Int): NavHostFragment? =
        fragmentManager.findFragmentByTag(graphId.toString()) as? NavHostFragment

    private fun getVisible(): NavHostFragment? =
        fragmentManager.fragments
            .filterIsInstance<NavHostFragment>()
            .firstOrNull(NavHostFragment::isVisible)
}