package com.example.friendzone.nav.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.nav.routes.HomeRouteScreen
import com.example.friendzone.presentation.screens.post.CommentsScreen
import com.example.friendzone.presentation.screens.story.AddStory
import com.example.friendzone.presentation.screens.story.AllStory
import com.example.friendzone.presentation.screens.user.OtherUsers

fun NavGraphBuilder.homeNavGraph(rootNavController: NavHostController) {
    navigation(
        route = Graph.HomeGraph, startDestination = HomeRouteScreen.AddStory.route
    ) {
        composable(
            route = HomeRouteScreen.AddStory.route
        ) {
            AddStory(navController = rootNavController)
        }
        composable(
            route = HomeRouteScreen.AllStory.route
        ) {
            val data = it.arguments!!.getString("all_story")
            AllStory(navController = rootNavController, uid = data!! )
        }
        composable(
            route = HomeRouteScreen.CommentDetail.route
        ) {
            val data = it.arguments!!.getString("comment_detail")
            CommentsScreen(navController = rootNavController, postId = data!! )
        }
        composable(
            route = HomeRouteScreen.OtherProfile.route
        ) {
            val data = it.arguments!!.getString("data")
            OtherUsers(navController = rootNavController, uid = data!!)
        }
    }
}