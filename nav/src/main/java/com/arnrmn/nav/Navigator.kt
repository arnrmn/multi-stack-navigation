package com.arnrmn.nav

import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment

class Navigator(
    @IdRes private val containerViewId: Int,
    private val fragmentManager: FragmentManager
) {
    private val listeners = mutableListOf<(Int) -> Unit>()
    private var mainGraph: Int = NOT_SET
    private var visibleGraph: Int = NOT_SET

    fun initNavigation(vararg graphs: Graph) {
        fragmentManager.beginTransaction()
            .also { transaction ->
                graphs.forEachIndexed { index, graph ->
                    val host = NavHostFragment.create(graph.graphId, graph.arguments)
                    transaction.add(containerViewId, host, graph.graphId.toString())
                    if (index == 0) {
                        mainGraph = graph.graphId
                        visibleGraph = graph.graphId
                        transaction.show(host)
                    } else {
                        transaction.hide(host)
                    }
                }
            }
            .setReorderingAllowed(true)
            .commit()
    }

    /**
     * Shows navigation host fragment with provided graph id if such exists.
     * @return true if transaction commit was executed, false otherwise.
     */
    //todo notify listeners
    fun show(@NavigationRes graphId: Int): Boolean {
        val visible = getVisible()
        val target = getHost(graphId)
        return if (target != null && visible != target) {
            fragmentManager.beginTransaction()
                .show(target)
                .also { transaction -> if (visible != null) transaction.hide(visible) }
                .setReorderingAllowed(true)
                .runOnCommit { visibleGraph = graphId }
                .runOnCommit { listeners.forEach { action -> action(graphId) } }
                .commit()
            true
        } else {
            false
        }
    }

    /**
     * Returns true when consumes back action, false otherwise.
     */
    fun navigateUp(): Boolean {
        return getVisible()?.let { visibleHost ->
            if (visibleHost.tag == mainGraph.toString()) {
                visibleHost.navController.navigateUp()
            } else if (!visibleHost.navController.navigateUp()) {
                show(mainGraph)
            } else {
                true
            }
        } ?: false
    }

    fun addTopGraphChangeListener(listener: (Int) -> Unit) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    fun getVisibleGraphId(): Int = visibleGraph

    fun getMainGraphId(): Int = mainGraph

    private fun getHost(@NavigationRes graphId: Int): NavHostFragment? {
        return fragmentManager.findFragmentByTag(graphId.toString()) as? NavHostFragment
    }

    private fun getVisible(): NavHostFragment? {
        return fragmentManager.fragments
            .filterIsInstance<NavHostFragment>()
            .firstOrNull(NavHostFragment::isVisible)
    }

    companion object {
        private const val NOT_SET = -1
    }
}