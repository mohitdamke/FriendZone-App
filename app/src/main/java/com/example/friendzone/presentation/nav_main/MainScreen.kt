package com.example.friendzone.presentation.nav_main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.friendzone.nav.bottom_navigation.BottomNavigationBar
import com.example.friendzone.nav.graph.MainNavGraph
import com.example.friendzone.ui.theme.DarkBlack

@Composable
fun MainScreen(
    rootNavController: NavHostController = rememberNavController(),
    homeNavController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = homeNavController,
                modifier = Modifier.background(DarkBlack)
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(DarkBlack)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                        )
                    ),
            ) {
                MainNavGraph(
                    rootNavController = rootNavController,
                    homeNavController = homeNavController
                )
            }
        },

        )
}
