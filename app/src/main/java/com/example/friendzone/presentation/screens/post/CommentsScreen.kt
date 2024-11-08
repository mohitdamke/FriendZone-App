package com.example.friendzone.presentation.screens.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.friendzone.data.model.CommentModel
import com.example.friendzone.util.SharedPref
import com.example.friendzone.viewmodel.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CommentsScreen(
    navController: NavController,
    postId: String,
    viewModel: HomeViewModel = HomeViewModel(),
) {
    val context = LocalContext.current
    var comments by remember { mutableStateOf(listOf<CommentModel>()) }

    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    LaunchedEffect(postId) {
        viewModel.fetchComments(postId) { fetchedComments ->
            comments = fetchedComments
        }
    }

    val commentSize = comments?.size?.toString() ?: 0
    Column(modifier = Modifier.fillMaxSize()) {

        Comments(
            comments = comments,
            onBackClick = { navController.navigateUp() },
            onCommentAdded = { commentText ->
                val newComment = CommentModel(
                    userId = FirebaseAuth.getInstance().currentUser!!.uid,
                    text = commentText
                )
                viewModel.addComment(
                    userId = currentUserId,
                    postId = postId,
                    commentText = commentText,
                    username = SharedPref.getUserName(context),
                    name = SharedPref.getName(context),
                    image = SharedPref.getImageUrl(context),
                    timeStamp = System.currentTimeMillis().toString()
                )
                comments = comments + newComment
            })
    }
}
