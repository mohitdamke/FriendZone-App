package com.example.friendzone.common

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.data.model.CommentModel
import com.example.friendzone.data.model.PostModel
import com.example.friendzone.data.model.UserModel
import com.example.friendzone.dimension.FontDim
import com.example.friendzone.dimension.TextDim
import com.example.friendzone.nav.routes.HomeRouteScreen
import com.example.friendzone.nav.routes.MainRouteScreen
import com.example.friendzone.viewmodel.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.absoluteValue

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
    val minimumLineLength = 2 // Change this to your desired value

    // Adding States
    var expandedState by remember { mutableStateOf(false) }
    var showReadMoreButtonState by remember { mutableStateOf(false) }
    val maxLines = if (expandedState) 200 else minimumLineLength


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
    var likeCount by remember { mutableIntStateOf(currentPost?.likes?.size ?: 0) }
    var isLiked by remember { mutableStateOf(currentPost?.likes?.containsKey(currentUserId) == true) }
    val savedPostIds by homeViewModel.savedPostIds.observeAsState(emptyList())

    // Check if the current post is in the list of saved posts
    val isSaved = post?.storeKey?.let { savedPostIds.contains(it) } ?: false

    var comments by remember { mutableStateOf(listOf<CommentModel>()) }
    var commentCount by remember { mutableIntStateOf(0) }
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
    Spacer(modifier = modifier.padding(top = 4.dp))

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.Top,
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
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = users.name,
                    fontSize = TextDim.titleTextSize,
                    fontFamily = FontDim.Bold,
                    color = White,
                    modifier = modifier.padding(start = 10.dp)
                )
                Text(
                    text = users.userName,
                    fontSize = TextDim.secondaryTextSize,
                    fontFamily = FontDim.Medium,
                    color = White,
                    modifier = modifier.padding(start = 10.dp)
                )
            }

            Spacer(modifier = modifier.weight(1f))


            Spacer(modifier = modifier.padding(start = 6.dp))

        }

        Text(
            text = post.post,
            fontSize = TextDim.titleTextSize,
            fontFamily = FontDim.Normal,
            color = White,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                if (textLayoutResult.lineCount > minimumLineLength - 1) {           //Adding this check to avoid ArrayIndexOutOfBounds Exception
                    if (textLayoutResult.isLineEllipsized(minimumLineLength - 1)) showReadMoreButtonState =
                        true
                }
            }
        )
        if (showReadMoreButtonState) {
            Text(
                text = if (expandedState) "Read Less" else "Read More",
                color = Color.Gray,

                modifier = Modifier.clickable {
                    expandedState = !expandedState
                },

                style = MaterialTheme.typography.bodySmall

            )
        }
        if (post.images?.isNotEmpty() == true) {

            Spacer(modifier = modifier.padding(top = 10.dp))
        }
        if (post.images?.isNotEmpty() == true) {
            HorizontalPager(
                state = imagePagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .size(400.dp)
                        .graphicsLayer {
                            // Calculate the absolute offset for the current page from the
                            // scroll position. We use the absolute value which allows us to mirror
                            // any effects for both directions
                            val pageOffset = (
                                    (imagePagerState.currentPage - page) + imagePagerState
                                        .currentPageOffsetFraction
                                    ).absoluteValue

                            // We animate the alpha, between 50% and 100%
                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = post.images?.get(page)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(400.dp),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Spacer(modifier = modifier.padding(top = 8.dp))
            if ((post.images.size ?: 0) > 1) {
                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(imagePagerState.pageCount) { iteration ->
                        val color =
                            if (imagePagerState.currentPage == iteration) Color.LightGray else Color.DarkGray
                        Box(
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(10.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = modifier.padding(top = 10.dp))

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(modifier = modifier.padding(start = 10.dp))
            Icon(imageVector = if (isLiked) Icons.Filled.ThumbUp
            else Icons.Outlined.ThumbUp, contentDescription = null, tint = if (isLiked) White
            else White, modifier = modifier
                .size(22.dp)
                .clickable {
                    if (currentUserId.isNotEmpty()) {
                        homeViewModel.toggleLike(
                            postId = post?.storeKey ?: "", userId = currentUserId
                        )
                    }
                })

            Text(
                text = "$likeCount",
                fontSize = TextDim.bodyTextSize,
                fontFamily = FontDim.Medium,
                color = White,
                modifier = modifier.padding(start = 6.dp)
            )
            Spacer(modifier = modifier.padding(start = 10.dp))

            Icon(
                imageVector = Icons.Outlined.ModeComment,
                contentDescription = null,
                modifier = modifier
                    .size(22.dp)
                    .clickable {
                        navController.navigate(
                            HomeRouteScreen.CommentDetail.route.replace(
                                "{comment_detail}", post.storeKey ?: ""
                            )
                        )
                    },
                tint = White
            )
            Text(
                text = "$commentCount",
                fontSize = TextDim.bodyTextSize,
                fontFamily = FontDim.Medium,
                color = White,
                modifier = modifier.padding(start = 6.dp)

            )

            Spacer(modifier = modifier.weight(1f))
            Icon(imageVector = if (isSaved) Icons.Default.Bookmark
            else Icons.Default.BookmarkBorder,
                contentDescription = null,
                tint = White,  // Adjust color as needed
                modifier = modifier
                    .size(22.dp)
                    .clickable {
                        post?.storeKey?.let {
                            homeViewModel.toggleSavePost(
                                postId = it, currentUserId = currentUserId, context = context
                            )
                        }

                    })
            Spacer(modifier = modifier.padding(start = 4.dp))
        }
    }
    Spacer(modifier = modifier.padding(top = 4.dp))
    Divider(modifier = modifier.height(1.dp), color = Gray)
}




















