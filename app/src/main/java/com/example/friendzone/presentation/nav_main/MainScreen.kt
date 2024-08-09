package com.example.friendzone.presentation.nav_main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.friendzone.nav.graph.MainNavGraph
import com.example.friendzone.nav.bottom_navigation.BottomNavigationBar

@Composable
fun MainScreen(
    rootNavController : NavHostController= rememberNavController(),
    homeNavController : NavHostController= rememberNavController()
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = homeNavController) },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                MainNavGraph(rootNavController = rootNavController , homeNavController = homeNavController )
            }
        },

    )
}
