package com.example.friendzone.nav.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.nav.routes.MainRouteScreen
import com.example.friendzone.presentation.screens.post.AddPostScreen
import com.example.friendzone.presentation.screens.chat.ChatScreen
import com.example.friendzone.presentation.screens.home.HomeScreen
import com.example.friendzone.presentation.screens.profile.ProfileScreen
import com.example.friendzone.presentation.screens.search.SearchScreen

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    rootNavController: NavHostController,
    homeNavController: NavHostController
) {


    NavHost(
        navController = homeNavController,
        route = Graph.MainScreenGraph,
        startDestination = MainRouteScreen.Home.route,
    ) {
        composable(route = MainRouteScreen.Home.route){
            HomeScreen(navController = rootNavController)
        }

        composable(route = MainRouteScreen.Search.route){
            SearchScreen()
        }

        composable(route = MainRouteScreen.AddPost.route){
            AddPostScreen()
        }

        composable(route = MainRouteScreen.Chat.route){
            ChatScreen()
        }

        composable(route = MainRouteScreen.Profile.route){
            ProfileScreen()
        }
    }
}