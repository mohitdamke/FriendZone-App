package com.example.friendzone.nav.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.nav.routes.HomeRouteScreen
import com.example.friendzone.nav.routes.SearchRouteScreen
import com.example.friendzone.presentation.screens.home.DetailScreen
import com.example.friendzone.presentation.screens.user.OtherUsers

fun NavGraphBuilder.searchNavGraph(rootNavController: NavHostController) {
    navigation(
        route = Graph.SearchGraph, startDestination = SearchRouteScreen.OtherProfile.route
    ) {
        composable(
            route = SearchRouteScreen.OtherProfile.route
        ) {
            val data = it.arguments!!.getString("data")
            OtherUsers(navController = rootNavController, uid = data!!)
        }
    }
}