package com.example.friendzone.nav.bottom_navigation

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
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
        containerColor = White,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null,
                        modifier = modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
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
                    selectedIconColor = Blue40,
                    unselectedIconColor = Blue80,
                    indicatorColor = Black40
                ), modifier = modifier
                    .weight(1f)
                    .clip(shape = CircleShape)
                    .clip(RoundedCornerShape(100.dp))
            )
        }
    }

}


sealed class BottomNavItem(val route: String, val icon: ImageVector) {
    object Home : BottomNavItem(MainRouteScreen.Home.route, Icons.Default.Home)
    object Search : BottomNavItem(MainRouteScreen.Search.route, Icons.Default.Search)
    object AddPost :
        BottomNavItem(MainRouteScreen.AddPost.route, Icons.Default.AddCircle)

    object ChatPeople : BottomNavItem(MainRouteScreen.Chat.route, Icons.Default.Chat)
    object Profile : BottomNavItem(MainRouteScreen.Profile.route, Icons.Default.Person)
}