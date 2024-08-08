package com.example.friendzone.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.friendzone.navigation.BottomNavItem
import com.example.friendzone.presentation.screens.addPost.AddPostScreen
import com.example.friendzone.presentation.screens.chat.ChatScreen
import com.example.friendzone.presentation.screens.home.HomeScreen
import com.example.friendzone.presentation.screens.profile.ProfileScreen
import com.example.friendzone.presentation.screens.search.SearchScreen

@Composable
fun BottomNavigation(navHostController: NavHostController) {
    val nav = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = BottomNavItem.Home.route,
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen()
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen()
        }
        composable(BottomNavItem.AddPost.route) {
            AddPostScreen()
        }
        composable(BottomNavItem.ChatPeople.route) {
            ChatScreen()
        }
        composable(BottomNavItem.Profile.route) {
            ProfileScreen()
        }
    }
}
