package com.example.friendzone.presentation.screens.post

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.data.model.CommentModel
import com.example.friendzone.dimension.FontDim
import com.example.friendzone.dimension.TextDim
import com.example.friendzone.ui.theme.Black
import com.example.friendzone.ui.theme.DarkBlack
import com.example.friendzone.ui.theme.White
import com.example.friendzone.ui.theme.brushAddPost


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Comments(
    modifier: Modifier = Modifier,
    comments: List<CommentModel>,
    onCommentAdded: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var commentText by remember { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val context = LocalContext.current

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkBlack,
                    titleContentColor = White,
                    actionIconContentColor = White,
                    navigationIconContentColor = White,
                    scrolledContainerColor = DarkBlack,
                ),
                title = {
                    Text(
                        text = "Post Comments",
                        maxLines = 1,
                        letterSpacing = 1.sp, fontSize = TextDim.titleTextSize,
                        overflow = TextOverflow.Visible,
                        fontFamily = FontDim.Bold,
                    )
                }, navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = modifier.size(28.dp)

                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            CommentOutlineText(
                modifier = modifier,
                value = commentText,
                label = "Comment",
                onValueChange = { commentText = it },
                onSendClick = {
                    if (commentText.isNotEmpty()) {
                        onCommentAdded(commentText)
                        commentText = ""
                    }
                    Toast.makeText(context, "Comment added", Toast.LENGTH_SHORT).show()
                },
            )
        }) { paddingValues ->

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkBlack)
        ) {
            item {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(text = "Comments", fontWeight = FontWeight.Bold)

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
                                        text = comment.name,
                                        fontSize = TextDim.titleTextSize,
                                        fontFamily = FontDim.Bold,
                                        color = White
                                    )

                                }
                                Text(
                                    text = comment.text,
                                    fontSize = TextDim.titleTextSize,
                                    fontFamily = FontDim.Medium,
                                    color = White
                                )
                            }
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
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    label: String
) {

    Column(modifier = modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            singleLine = true,
            placeholder = {
                Text(
                    text = "Type your $label here...",
                    fontSize = TextDim.secondaryTextSize,
                    fontFamily = FontDim.Medium,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
            },
            trailingIcon = {
                Box(
                    modifier = Modifier
                        .size(40.dp) // Size of the outer circle
                        .clip(CircleShape)
                        .background(brushAddPost) // Background brush for gradient or color
                        .padding(8.dp) // Padding inside the circle
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "Send",
                        modifier = modifier
                            .size(30.dp)
                            .align(Alignment.Center)
                            .rotate(270f)
                            .clickable {
                                onSendClick()
                            }, tint = White
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedPlaceholderColor = Color.Gray,
                focusedPlaceholderColor = Color.Gray,
                disabledContainerColor = Color.Gray,
                focusedBorderColor = Gray,
                unfocusedBorderColor = Gray,
                focusedContainerColor = DarkBlack,
                unfocusedContainerColor = DarkBlack,
                focusedTextColor = White,
                unfocusedTextColor = White
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            ),
            modifier = modifier
                .background(Black)
                .fillMaxWidth()
                .padding(10.dp)
                .padding(bottom = 20.dp),
            shape = RoundedCornerShape(100.dp),
            minLines = 1
        )
    }

}