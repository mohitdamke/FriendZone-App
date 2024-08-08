package com.example.friendzone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.friendzone.presentation.screens.MainScreen
import com.example.friendzone.presentation.screens.Splash
import com.example.friendzone.presentation.screens.addPost.AddPostScreen
import com.example.friendzone.presentation.screens.auth.Login
import com.example.friendzone.presentation.screens.auth.Register
import com.example.friendzone.presentation.screens.chat.ChatScreen
import com.example.friendzone.presentation.screens.home.DetailScreen
import com.example.friendzone.presentation.screens.home.HomeScreen
import com.example.friendzone.presentation.screens.profile.ProfileScreen
import com.example.friendzone.presentation.screens.search.SearchScreen


@Composable
fun NavigationGraph(
    navController: NavHostController,
) {

    NavHost(navController = navController, startDestination = Routes.Splash.routes) {
        composable(Routes.Splash.routes) {
            Splash(navController = navController)
        }

        composable(Routes.MainScreen.routes) {
            MainScreen()
        }

        composable(Routes.DetailScreen.routes) {
            DetailScreen()
        }

        composable(Routes.Login.routes) {
            Login(navController = navController)
        }
        composable(Routes.Register.routes) {
            Register(navController = navController)
        }
        composable(Routes.BottomNav.routes) {
            BottomNavigationBar(navController = navController)
        }
        composable(BottomNavItem.Home.route) {
            HomeScreen(navController = navController)
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


@Composable
fun NavGffraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Splash.routes) {

        composable(route = Routes.Splash.routes) {
            Splash(navController = navController)
        }

//        composable(route = Routes.Home.routes) {
//            Home(navController = navController)
//        }
//
//        composable(route = Routes.Login.routes) {
//            Login(navController = navController)
//        }
//
//        composable(route = Routes.Register.routes) {
//            Register(navController = navController)
//        }
//
//        composable(route = Routes.Search.routes) {
//            Search(navController = navController)
//        }
//
//
//        composable(route = Routes.Profile.routes) {
//            Profile(navController = navController)
//        }
//
//        composable(route = Routes.AddThread.routes) {
//            AddThreads(navController = navController)
//        }
//
//        composable(route = Routes.AddStory.routes) {
//            AddStory(navController = navController)
//        }
//
//        composable(route = Routes.BottomNav.routes) {
//            BottomNav(navController = navController)
//        }
//
//        composable(route = Routes.OtherUsers.routes) {
//            val data = it.arguments!!.getString("data")
//            OtherUsers(navController = navController, uid = data!!)
//        }
//        composable(route = Routes.StoryDetail.routes) {
////            val data = it.arguments!!.getString("data")
//            StoryDetail(navController = navController)
//        }
//
//        composable(route = Routes.AllStory.routes) {
//            val data = it.arguments!!.getString("all_story")
//            AllStory(navController = navController, uid = data!!)
//        }
//
//        composable(route = Routes.Setting.routes) {
//            Setting(navController = navController)
//        }
//
//        composable(route = Routes.Comments.routes) {
//            val data = it.arguments!!.getString("data")
//            CommentsScreen(navController = navController, threadId = data!!)
//        }
//
//        composable(route = Routes.Saved.routes) {
//            Saved(navController = navController)
//        }
//
//        composable(route = Routes.EditProfile.routes) {
//            EditProfile(navController = navController)
//        }
//
//        composable(route = Routes.ChatGemini.routes) {
//            AiChatScreen(navController = navController)
//        }
//
//        composable(route = Routes.ChatPeople.routes) {
//            val data = it.arguments!!.getString("data")
//            ChatPeople(navController = navController, uid = data!!)
//        }
//
//        composable(route = Routes.AllChat.routes) {
//            AllChatScreen(navController = navController)
//        }


    }
}