package com.example.friendzone.nav.bottom_navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.friendzone.nav.routes.MainRouteScreen
import com.example.friendzone.ui.theme.Black40
import com.example.friendzone.ui.theme.Blue40
import com.example.friendzone.ui.theme.Blue80
import com.example.friendzone.ui.theme.brushAddPost

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: Boolean = false
) {

    val screens = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.AddPost,
        BottomNavItem.ChatPeople,
        BottomNavItem.Profile
    )

    NavigationBar(
        modifier = modifier,
        containerColor = Black,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    if (screen is BottomNavItem.AddPost) {
                        // Add a circular background only for AddPost
                        Box(
                            modifier = Modifier
                                .size(56.dp) // Size of the outer circle
                                .clip(CircleShape)
                                .background(brushAddPost) // Background brush for gradient or color
                                .padding(8.dp) // Padding inside the circle
                        ) {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = null,
                                tint = White, // Icon color
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    } else {
                        // Default styling for other icons
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
        },
        selected = currentRoute == screen.route,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        colors = NavigationBarItemDefaults.colors(
            unselectedTextColor = Gray,
            selectedTextColor = Black,
            selectedIconColor = if (screen is BottomNavItem.AddPost) White else White,
            unselectedIconColor = if (screen is BottomNavItem.AddPost) White else Gray,
            indicatorColor = Transparent
        ), modifier = modifier
        .weight(1f)
        .clip(shape = CircleShape)
        .clip(
            shape = if (screen is BottomNavItem.AddPost) CircleShape else RoundedCornerShape(
                100.dp
            )
        )
        )
    }
}

}


sealed class BottomNavItem(val route: String, val icon: ImageVector) {
    object Home : BottomNavItem(MainRouteScreen.Home.route, Icons.Outlined.Home)
    object Search : BottomNavItem(MainRouteScreen.Search.route, Icons.Outlined.Search)
    object AddPost :
        BottomNavItem(MainRouteScreen.AddPost.route, Icons.Outlined.Add)

    object ChatPeople : BottomNavItem(MainRouteScreen.Chat.route, Icons.Outlined.Message)
    object Profile : BottomNavItem(MainRouteScreen.Profile.route, Icons.Outlined.AccountCircle)
}