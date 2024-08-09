package com.example.friendzone.nav.routes

object Graph{
    const val SplashGraph = "splashGraph"
    const val RootGraph = "rootGraph"
    const val AuthGraph = "authGraph"
    const val MainScreenGraph = "mainScreenGraph"
    const val HomeGraph = "homeGraph"
}

sealed class SplashRouteScreen(val route: String) {
    object Splash : AuthRouteScreen("splash")
}

sealed class AuthRouteScreen(val route: String) {
    object Login : AuthRouteScreen("login")
    object Register : AuthRouteScreen("register")
}

sealed class MainRouteScreen(val route: String) {
    object Home : MainRouteScreen("home")
    object Search : MainRouteScreen("search")
    object AddPost : MainRouteScreen("add_post")
    object Chat : MainRouteScreen("chat")
    object Profile : MainRouteScreen("profile")
}

sealed class HomeRouteScreen(val route: String) {
    object  HomeDetail : AuthRouteScreen("home_detail")
}
