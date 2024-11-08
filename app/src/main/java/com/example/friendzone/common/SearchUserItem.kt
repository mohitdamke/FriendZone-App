package com.example.friendzone.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.data.model.UserModel
import com.example.friendzone.dimension.FontDim
import com.example.friendzone.dimension.TextDim
import com.example.friendzone.nav.routes.SearchRouteScreen
import com.example.friendzone.ui.theme.DarkBlack
import com.example.friendzone.ui.theme.White

@Composable
fun SearchUserItem(
    modifier: Modifier = Modifier,
    users: UserModel,
    navController: NavController,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkBlack)
            .padding(2.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    val routes = SearchRouteScreen.OtherProfile.route.replace("{data}", users.uid)
                    navController.navigate(routes)
                }
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = users.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(44.dp), contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp)
            ) {
                Text(
                    text = users.name, fontSize = TextDim.titleTextSize,
                    fontFamily = FontDim.Bold,
                    color = White
                )
                Text(
                    text = users.bio, fontSize = TextDim.tertiaryTextSize,
                    fontFamily = FontDim.Medium,
                    color = White
                )
            }
        }


    }
}




















