package com.example.friendzone.nav.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.nav.routes.SplashRouteScreen
import com.example.friendzone.presentation.screens.splash.SplashScreen

fun NavGraphBuilder.splashNavGraph(rootNavController: NavHostController) {
    navigation(
        route = Graph.SplashGraph, startDestination = SplashRouteScreen.Splash.route
    ) {
        composable(
            route = SplashRouteScreen.Splash.route
        ) {
            SplashScreen(navController = rootNavController)
        }

    }
}