package com.example.friendzone.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.data.model.StoryModel
import com.example.friendzone.data.model.UserModel
import com.example.friendzone.viewmodel.story.AddStoryViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CurrentUserStoryItem(
    modifier: Modifier = Modifier,
    story: StoryModel,
) {
    val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    if (story.userId == currentUser) {

        Column(modifier = modifier) {
            if (story.imageStory != "") {

                Image(
                    painter = rememberAsyncImagePainter(model = story.imageStory),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(width = 2.dp, color = Red, shape = CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
        IconButton(onClick = {
        }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    } else {
        Column(modifier = modifier) {

            Image(
                painter = rememberAsyncImagePainter(model = story.imageStory),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
               , contentScale = ContentScale.Crop
            )
        }
    }
    IconButton(onClick = {
    }) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
    }

}




















