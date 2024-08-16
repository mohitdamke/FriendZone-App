package com.example.friendzone.presentation.screens.post

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.common.FormatTimestamp
import com.example.friendzone.data.model.CommentModel
import com.example.friendzone.data.model.UserModel
import com.google.firebase.auth.FirebaseAuth



@Composable
fun Comments(
    modifier: Modifier = Modifier,
    comments: List<CommentModel>,
    onCommentAdded: (String) -> Unit
) {

    val context = LocalContext.current
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Comments", fontWeight = FontWeight.Bold)

                var commentText by remember { mutableStateOf("") }

                Row(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    CommentOutlineText(
                        value = commentText,
                        label = "Comment",
                        onValueChange = { commentText = it },
                        icons = Icons.Default.AddComment
                    )
                    Button(onClick = {
                        if (commentText.isNotEmpty()) {
                            onCommentAdded(commentText)
                            commentText = ""
                        }
                        Toast.makeText(context, "Comment added", Toast.LENGTH_SHORT).show()
                    }) {
                        Text(text = "Post")
                    }
                }

                comments.forEach { comment ->
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(comment.image),
                            contentDescription = null, modifier = Modifier
                                .clip(CircleShape)
                                .size(44.dp), contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row {

                                Text(
                                    text = comment.username,
                                    fontSize = 16.sp,
                                )

                            }
                            Text(
                                text = comment.text,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                }


            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommentOutlineText(
    modifier: Modifier = Modifier,
    value: String,
    icons: ImageVector,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = icons,
                contentDescription = "",
                modifier = Modifier.padding(10.dp),
                tint = Color.Black
            )
        },
        label = {
            Text(
                text = "Type your $label",
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = Color.Black,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
        ),
        modifier = modifier,
        minLines = 1
    )
}
