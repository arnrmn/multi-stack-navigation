package com.arnrmn.nav

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment

class MultiNavContainer(context: Context, attrRes: AttributeSet? = null) : FrameLayout(context, attrRes) {

    fun addBranch(
        @NavigationRes graphId: Int,
        isMain: Boolean = true,
        startDestinationArgs: Bundle? = null
    ) {
        val host = NavHostFragment.create(graphId, startDestinationArgs)
        (context as? FragmentActivity)
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.add(id, host, graphId.toString())
            ?.hide(host)
            ?.commit()
    }

    fun switchTo(@NavigationRes graphId: Int) {
        val target = getHost(graphId)
        val visible = getVisible()
        if (target != null && target != visible) {
            val transaction = (context as? FragmentActivity)
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.show(target)
            if (visible != null) transaction?.hide(visible)
            transaction?.commit()
        }
    }

    private fun getHost(@NavigationRes graphId: Int): Fragment? {
        return (context as? FragmentActivity)
            ?.supportFragmentManager
            ?.findFragmentByTag(graphId.toString())
    }

    private fun getVisible(): Fragment? {
        return (context as? FragmentActivity)
            ?.supportFragmentManager
            ?.fragments
            ?.firstOrNull(Fragment::isVisible)
    }
}