package com.example.friendzone.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.friendzone.R
import com.example.friendzone.navigation.BottomNavItem
import com.example.friendzone.navigation.BottomNavigationBar
import com.example.friendzone.presentation.BottomNavigation
import com.example.friendzone.presentation.screens.addPost.AddPostScreen
import com.example.friendzone.presentation.screens.chat.ChatScreen
import com.example.friendzone.presentation.screens.home.HomeScreen
import com.example.friendzone.presentation.screens.profile.ProfileScreen
import com.example.friendzone.presentation.screens.search.SearchScreen
import com.example.friendzone.presentation.top.TopBar

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                BottomNavigation(navHostController = navController)
            }
        },

    )
}
