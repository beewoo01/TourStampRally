package com.sdin.tourstamprally.utill.navutil

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.IdRes
import androidx.core.content.withStyledAttributes
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.NavDestination
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sdin.tourstamprally.R
import java.lang.ref.WeakReference
import java.util.*

object DialogNavigator {

    fun setupWithNavController(
        bottomNavigationView: BottomNavigationView,
        navController: NavController
    ) {
        bottomNavigationView.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(
                item,
                navController
            )
        }
        val weakReference = WeakReference(bottomNavigationView)
        navController.addOnDestinationChangedListener(
            object : OnDestinationChangedListener {
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination, arguments: Bundle?
                ) {
                    val view = weakReference.get()
                    if (view == null) {
                        navController.removeOnDestinationChangedListener(this)
                        return
                    }
                    val menu = view.menu
                    var h = 0
                    val size = menu.size()
                    while (h < size) {
                        val item = menu.getItem(h)
                        if (matchDestination(destination, item.itemId)) {
                                item.isChecked = true

                        }
                        h++
                    }
                }
            })
    }

    fun matchDestination(
        destination: NavDestination,
        @IdRes destId: Int
    ): Boolean {
        var currentDestination: NavDestination? = destination
        while (currentDestination!!.id != destId && currentDestination.parent != null) {
            currentDestination = currentDestination.parent
        }
        return currentDestination.id == destId
    }
}