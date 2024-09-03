package com.example.friendzone.nav.graph

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.friendzone.nav.routes.AuthRouteScreen
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.presentation.screens.auth.LoginScreen
import com.example.friendzone.presentation.screens.auth.RegisterScreen

fun NavGraphBuilder.authNavGraph(rootNavController: NavHostController) {
    navigation(
        route = Graph.AuthGraph, startDestination = AuthRouteScreen.Login.route
    ) {

        composable(
            route = AuthRouteScreen.Login.route
        ) {
            LoginScreen(navController = rootNavController)
        }

        composable(
            route = AuthRouteScreen.Register.route
        ) {
            RegisterScreen(navController = rootNavController)
        }
    }
}