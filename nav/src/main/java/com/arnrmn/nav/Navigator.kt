package com.arnrmn.nav

import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment

class Navigator(
    @IdRes private val containerViewId: Int,
    private val fragmentManager: FragmentManager
) {

    fun initNavigation(vararg graphs: Graph) {
        fragmentManager.beginTransaction()
            .also { transaction ->
                graphs.forEachIndexed { index, graph ->
                    val host = NavHostFragment.create(graph.graphId, graph.arguments)
                    transaction.add(containerViewId, host, graph.graphId.toString())
                    if (index == 0) transaction.show(host) else transaction.hide(host)
                }
            }.commit()
    }

    /**
     * Shows navigation host fragment with provided graph id if such exists.
     * */
    fun show(@NavigationRes graphId: Int): Boolean {
        val visible = getVisible()
        val target = getHost(graphId)
        return if (target != null && visible != target) {
            fragmentManager.beginTransaction()
                .show(target)
                .also { transaction -> if (visible != null) transaction.hide(visible) }
                .commit()
            true
        } else {
            false
        }
    }

    /**
     * Returns true when consumes back action, false otherwise.
     */
    fun navigateUp(): Boolean = getVisible()?.navController?.navigateUp() ?: false

    private fun getHost(@NavigationRes graphId: Int): NavHostFragment? =
        fragmentManager.findFragmentByTag(graphId.toString()) as? NavHostFragment

    private fun getVisible(): NavHostFragment? =
        fragmentManager.fragments
            .filterIsInstance<NavHostFragment>()
            .firstOrNull(NavHostFragment::isVisible)
}