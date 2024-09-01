package com.example.friendzone.common

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.data.model.CommentModel
import com.example.friendzone.data.model.PostModel
import com.example.friendzone.data.model.UserModel
import com.example.friendzone.nav.routes.HomeRouteScreen
import com.example.friendzone.nav.routes.MainRouteScreen
import com.example.friendzone.viewmodel.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostItem(
    modifier: Modifier = Modifier,
    post: PostModel?,
    users: UserModel?,
    navController: NavController,
    navController2: NavController = rememberNavController(),
    homeViewModel: HomeViewModel,
) {

    val context = LocalContext.current
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

    val imagePagerState = rememberPagerState(pageCount = {
        post?.images?.size ?: 0
    })

    val postsAndUsers by homeViewModel.postsAndUsers.observeAsState(emptyList())
    val currentPost = postsAndUsers.firstOrNull { it.first.storeKey == post?.storeKey }?.first

    if (post == null || users == null) {
        return
    }
    var likeCount by remember { mutableStateOf(currentPost?.likes?.size ?: 0) }
    var isLiked by remember { mutableStateOf(currentPost?.likes?.containsKey(currentUserId) == true) }
    val savedPostIds by homeViewModel.savedPostIds.observeAsState(emptyList())

    // Check if the current post is in the list of saved posts
    val isSaved = post?.storeKey?.let { savedPostIds.contains(it) } ?: false

    var comments by remember { mutableStateOf(listOf<CommentModel>()) }
    var commentCount by remember { mutableStateOf(0) }
    LaunchedEffect(currentPost) {
        currentPost?.let {
            homeViewModel.fetchComments(it.storeKey) { fetchedComments ->
                comments = fetchedComments
                commentCount = comments.size
                Log.d("PostItemTAG", "Updated comment count: $commentCount")
            }
            likeCount = it.likes.size
            isLiked = it.likes.containsKey(currentUserId)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(White)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(6.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = users.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp)
                        .clickable {
                            if (currentUserId.isNotEmpty() && users.uid == currentUserId) navController2.navigate(
                                MainRouteScreen.Profile.route
                            )
                            else {
                                navController.navigate(
                                    HomeRouteScreen.OtherProfile.route.replace(
                                        oldValue = "{other_profile}", newValue = users.uid
                                    )
                                )
                            }
                        },
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = users.userName,
                    fontSize = 20.sp,
                    modifier = modifier.padding(start = 10.dp)
                )

                Spacer(modifier = modifier.weight(1f))

                Icon(
                    imageVector = if (isSaved) Icons.Default.Bookmark
                    else Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    tint = Black,  // Adjust color as needed
                    modifier = modifier
                        .size(30.dp)
                        .clickable {
                            post?.storeKey?.let {
                                homeViewModel.toggleSavePost(
                                    postId = it,
                                    currentUserId = currentUserId,
                                    context = context
                                )
                            }

                        }
                )
                Spacer(modifier = modifier.padding(start = 6.dp))

            }

            if (post.images?.isNotEmpty() == true) {
                HorizontalPager(
                    state = imagePagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .height(200.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = post.images?.get(it)),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .size(370.dp),
                        contentScale = ContentScale.Crop,
                    )
                }
            }

            Column(
                modifier = modifier
                    .padding(start = 12.dp)
                    .padding(4.dp)
            ) {
                Text(text = post.post, fontSize = 18.sp, fontWeight = FontWeight.Normal)
            }

            Spacer(modifier = modifier.padding(top = 20.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = modifier.padding(start = 10.dp))

                Icon(imageVector = Icons.Default.AddComment,
                    contentDescription = null,
                    modifier = modifier
                        .size(30.dp)
                        .clickable {
                            navController.navigate(
                                HomeRouteScreen.CommentDetail.route.replace(
                                    "{comment_detail}", post.storeKey ?: ""
                                )
                            )
                        })
                Spacer(modifier = modifier.padding(start = 6.dp))
                Text(text = "$commentCount comment")
                Spacer(modifier = modifier.padding(start = 130.dp))

                Log.d("PostItemTAG", "Comment count: ${post.comments.size}")
                Text(text = "$likeCount Likes")
                Spacer(modifier = modifier.padding(start = 6.dp))
                Icon(imageVector = if (isLiked) Icons.Default.Favorite
                else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isLiked) Red
                    else Black,
                    modifier = modifier
                        .size(30.dp)
                        .clickable {
                            if (currentUserId.isNotEmpty()) {
                                homeViewModel.toggleLike(
                                    postId = post?.storeKey ?: "", userId = currentUserId
                                )
                            }
                        })
                Spacer(modifier = modifier.padding(top = 10.dp))

            }
        }
        Spacer(modifier = modifier.padding(top = 10.dp))
    }
}




















