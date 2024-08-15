package com.example.friendzone.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.data.model.UserModel
import com.example.friendzone.nav.routes.HomeRouteScreen
import com.example.friendzone.ui.theme.Blue40
import com.example.friendzone.ui.theme.PurpleGrey80

@Composable
fun UsersStoryHomeItem(
    modifier: Modifier = Modifier,
    users: UserModel,
    navHostController: NavController,
) {
    Column(
        modifier = modifier
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Image(
            painter = rememberAsyncImagePainter(model = users.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(width = 4.dp,
                    color = Blue40,
                    shape = CircleShape)
                .clickable {
                    val routes = HomeRouteScreen.AllStory.route.replace("{all_story}", users.uid)
                    navHostController.navigate(routes)
                },
            contentScale = ContentScale.Crop

        )
        Text(
            text = users.userName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}




















