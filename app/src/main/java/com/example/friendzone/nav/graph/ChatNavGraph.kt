package com.example.friendzone.nav.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.friendzone.geminiAi.presentation.AiChatScreen
import com.example.friendzone.nav.routes.ChatRouteScreen
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.presentation.screens.chat.ChatPeople
import com.example.friendzone.presentation.screens.user.OtherUsers

fun NavGraphBuilder.chatNavGraph(rootNavController: NavHostController) {
    navigation(
        route = Graph.ChatGraph, startDestination = ChatRouteScreen.AiChat.route
    ) {
        composable(
            route = ChatRouteScreen.AiChat.route
        ) {
            AiChatScreen(navController = rootNavController)
        }
        composable(
            route = ChatRouteScreen.OtherProfile.route
        ) {
            val data = it.arguments!!.getString("data")
            OtherUsers(navController = rootNavController, uid = data!!)
        }
        composable(
            route = ChatRouteScreen.OtherProfileChat.route
        ) {
            val data = it.arguments!!.getString("other_chat")
            ChatPeople(navController = rootNavController, uid = data!!)
        }
    }
}