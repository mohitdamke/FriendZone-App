package com.example.friendzone.nav.routes

object Graph {
    const val SplashGraph = "splashGraph"
    const val RootGraph = "rootGraph"
    const val AuthGraph = "authGraph"
    const val MainScreenGraph = "mainScreenGraph"
    const val HomeGraph = "homeGraph"
    const val SearchGraph = "searchGraph"
    const val ChatGraph = "chatGraph"
    const val ProfileGraph = "profileGraph"

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
    object AddStory : HomeRouteScreen("add_story")
    object CommentDetail : HomeRouteScreen("comment_detail/{comment_detail}")
    object AllStory : HomeRouteScreen("all_story/{all_story}")
    object OtherProfile : HomeRouteScreen("other_profile/{other_profile}")
}

sealed class SearchRouteScreen(val route: String) {
    object OtherProfile : SearchRouteScreen("other_profile/{data}")

}

sealed class ChatRouteScreen(val route: String) {

    object OtherProfile : ChatRouteScreen("other_profile/{data}")

    object OtherProfileChat : ChatRouteScreen("other_profile_chat/{other_chat}")

    object AiChat : ChatRouteScreen("ai_chat")

}

sealed class ProfileRouteScreen(val route: String) {

    object EditProfile : ProfileRouteScreen("edit_profile")

    object Settings : ProfileRouteScreen("settings")

    object SavedPosts : ProfileRouteScreen("saved_posts")

    object AddStory : ProfileRouteScreen("add_story")

}
