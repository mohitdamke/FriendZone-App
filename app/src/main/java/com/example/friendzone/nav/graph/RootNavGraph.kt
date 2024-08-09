package com.example.friendzone.nav.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.presentation.nav_main.MainScreen

@Composable
fun RootNavGraph(modifier: Modifier = Modifier) {
    val rootNavController: NavHostController = rememberNavController()

    NavHost(
        navController = rootNavController,
        route = Graph.RootGraph,
        startDestination = Graph.SplashGraph
    ) {
        splashNavGraph(rootNavController = rootNavController)

        authNavGraph(rootNavController = rootNavController)

        composable(route = Graph.MainScreenGraph){
            MainScreen(rootNavController = rootNavController)
        }

        homeNavGraph(rootNavController = rootNavController)
    }
}