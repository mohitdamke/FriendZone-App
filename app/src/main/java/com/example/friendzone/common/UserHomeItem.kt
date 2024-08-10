package com.example.friendzone.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.data.model.UserModel
import com.example.friendzone.nav.routes.HomeRouteScreen

@Composable
fun UserHomeItem(
    modifier: Modifier = Modifier,
    users: UserModel,
    navController: NavHostController,
) {

    Column(
        modifier = modifier
            .clickable {
                val routes = HomeRouteScreen.AllStory.route.replace("{all_story}", users.uid)
                navController.navigate(routes)
            }
            .padding(4.dp), verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = users.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(60.dp)
                .border(shape = CircleShape, color = Red, width = 4.dp)
        )
    }
}























