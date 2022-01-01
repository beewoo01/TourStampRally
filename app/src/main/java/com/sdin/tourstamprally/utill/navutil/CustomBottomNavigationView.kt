package com.sdin.tourstamprally.utill.navutil

import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.setupWithNavController(navController: NavController) {
    DialogNavigator.setupWithNavController( bottomNavigationView = this, navController = navController)
}