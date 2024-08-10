package com.example.friendzone.presentation.screens.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.friendzone.data.model.CommentModel

@Composable
fun Comments(
    modifier: Modifier = Modifier, comments: List<CommentModel>,
    onCommentAdded: (String) -> Unit
) {
    Column(modifier = modifier.padding(20.dp)) {
        var commentText by remember { mutableStateOf("") }
        Row {
            TextField(
                value = commentText,
                onValueChange = { commentText = it },
                placeholder = { Text(text = "Add a comment...") }
            )
            Button(onClick = {
                if (commentText.isNotEmpty()) {
                    onCommentAdded(commentText)
                    commentText = ""
                }
            }) {
                Text(text = "Post")
            }
        }
        comments.forEach { comment ->
            Text(text = "${comment.username} (${comment.name}): ${comment.text}")
        }
    }
}