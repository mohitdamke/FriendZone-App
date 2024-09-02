package com.example.friendzone.nav.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.presentation.nav_main.MainScreen

@Composable
fun RootNavGraph() {
    val rootNavController: NavHostController = rememberNavController()
    val homeNavController: NavHostController = rememberNavController()
    NavHost(
        navController = rootNavController,
        route = Graph.RootGraph,
        startDestination = Graph.MainScreenGraph
    ) {
        splashNavGraph(rootNavController = rootNavController)

        authNavGraph(rootNavController = rootNavController)

        composable(route = Graph.MainScreenGraph) {
            MainScreen(rootNavController = rootNavController)
        }

        homeNavGraph(rootNavController = rootNavController)
        searchNavGraph(rootNavController = rootNavController)
//        addPostNavGraph(rootNavController = rootNavController)
        chatNavGraph(rootNavController = rootNavController)
        profileNavGraph(rootNavController = rootNavController, homeNavController = homeNavController)
    }
}