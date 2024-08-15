package com.example.friendzone.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.data.model.StoryModel
import com.example.friendzone.data.model.UserModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun DetailStoryItem(
    modifier: Modifier = Modifier,
    story: StoryModel,
    users: UserModel,
    onDelete: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    if (story.imageStory != "") {
        Box(modifier = modifier) {

            Spacer(modifier = modifier.padding(10.dp))
            Image(
                painter = rememberAsyncImagePainter(model = story.imageStory),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .size(300.dp),
                contentScale = ContentScale.Fit
            )

            if (story.userId == users.uid && story.userId == currentUser) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null ,modifier = modifier.clickable {
                    onDelete()
                })
            } else {
                Unit
            }
        }
    }
}






















