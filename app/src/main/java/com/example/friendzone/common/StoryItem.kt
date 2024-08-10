package com.example.friendzone.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.data.model.StoryModel
import com.example.friendzone.data.model.UserModel
import com.example.friendzone.viewmodel.story.AddStoryViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun StoryItem(
    modifier: Modifier = Modifier,
    story: StoryModel,
) {
    if (story.imageStory != "") {
        Spacer(modifier = modifier.padding(start = 4.dp))
        Image(
            painter = rememberAsyncImagePainter(model = story.imageStory),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(width = 2.dp, color = Color.Red, shape = CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}






















