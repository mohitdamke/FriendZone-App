package com.example.friendzone.nav.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.nav.routes.HomeRouteScreen
import com.example.friendzone.nav.routes.ProfileRouteScreen
import com.example.friendzone.presentation.screens.home.DetailScreen
import com.example.friendzone.presentation.screens.post.Saved
import com.example.friendzone.presentation.screens.profile.EditProfile
import com.example.friendzone.presentation.screens.profile.Setting
import com.example.friendzone.presentation.screens.story.AddStory

fun NavGraphBuilder.profileNavGraph(
    rootNavController: NavHostController,
    homeNavController: NavHostController
) {
    navigation(
        route = Graph.ProfileGraph, startDestination = ProfileRouteScreen.AddStory.route
    ) {
        composable(
            route = ProfileRouteScreen.AddStory.route
        ) {
            AddStory(navController = rootNavController)
        }
        composable(
            route = ProfileRouteScreen.EditProfile.route
        ) {
            EditProfile(navController = rootNavController)
        }
        composable(
            route = ProfileRouteScreen.Settings.route
        ) {
            Setting(navController = rootNavController)
        }
        composable(
            route = ProfileRouteScreen.SavedPosts.route
        ) {
            Saved(navController = rootNavController, homeNavController = homeNavController)
        }
    }
}