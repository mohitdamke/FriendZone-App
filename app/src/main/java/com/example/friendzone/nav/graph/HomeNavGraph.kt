package com.example.friendzone.nav.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.nav.routes.HomeRouteScreen
import com.example.friendzone.presentation.screens.home.DetailScreen

fun NavGraphBuilder.homeNavGraph(rootNavController: NavHostController) {
    navigation(
        route = Graph.HomeGraph, startDestination = HomeRouteScreen.HomeDetail.route
    ) {
        composable(
            route = HomeRouteScreen.HomeDetail.route
        ) {
            DetailScreen()
        }
    }
}