package com.example.friendzone.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.R
import com.example.friendzone.data.model.PostModel
import com.example.friendzone.data.model.UserModel
import com.example.friendzone.nav.routes.HomeRouteScreen
import com.example.friendzone.nav.routes.MainRouteScreen
import com.example.friendzone.ui.theme.Blue100
import com.example.friendzone.ui.theme.Blue80
import com.example.friendzone.viewmodel.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostItem(
    modifier: Modifier = Modifier,
    post: PostModel?,
    users: UserModel?,
    navController: NavController,
    homeViewModel: HomeViewModel,
) {

    val context = LocalContext.current
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

    val imagePagerState = rememberPagerState(pageCount = {
        post?.images?.size ?: 0
    })

    val isLiked = post?.likes?.containsKey(currentUserId)
    val isSaved = users?.savedPosts?.containsKey(post?.storeKey)

    if (post == null || users == null) {
        return
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
                .padding(4.dp)
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
                            if (users!!.uid == currentUserId) {
                                navController.navigate(MainRouteScreen.Profile.route) {
                                    launchSingleTop = true
                                }
                            } else {
                                val routes = HomeRouteScreen.OtherProfile.route.replace(
                                    "{data}", users!!.uid
                                )
                                navController.navigate(routes) {
                                    launchSingleTop = true
                                }
                            }
                        },
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = users!!.userName,
                    fontSize = 20.sp,
                    modifier = modifier.padding(start = 10.dp)
                )

                Spacer(modifier = modifier.padding(start = 130.dp))

                Image(painter = if (isSaved == true) painterResource(id = R.drawable.bookmark) else painterResource(
                    id = R.drawable.save_instagram
                ), contentDescription = null, modifier = modifier
                    .size(24.dp)
                    .clickable {
                        homeViewModel.toggleSavePost(
                            postId = post.storeKey ?: "", context = context
                        )
                    })
            }

            if (post.images?.isNotEmpty() == true) {
                HorizontalPager(
                    state = imagePagerState, modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = post.images?.get(it)),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .size(370.dp),
                        contentScale = ContentScale.Fit,
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

                Image(painter = painterResource(id = R.drawable.comment),
                    contentDescription = null,
                    modifier = modifier
                        .size(24.dp)
                        .clickable {
                            navController.navigate(
                                HomeRouteScreen.CommentDetail.route.replace(
                                    "{comment_detail}", post.storeKey ?: ""
                                )
                            )
                        })
                Spacer(modifier = modifier.padding(start = 6.dp))
                Text(text = "${post.comments.size} comment")
                Spacer(modifier = modifier.padding(start = 130.dp))


                Text(text = "${post.likes.size} Likes")
                Spacer(modifier = modifier.padding(start = 6.dp))
                Image(painter = if (isLiked == true) painterResource(id = R.drawable.heartfilled) else painterResource(
                    id = R.drawable.heart
                ), contentDescription = null, modifier = modifier
                    .size(24.dp)
                    .clickable {
                        if (currentUserId.isNotEmpty()) {
                            homeViewModel.toggleLike(
                                postId = post?.storeKey ?: "", userId = currentUserId
                            )
                        }
                    })
            }
        }
        Spacer(modifier = modifier.padding(top = 10.dp))
    }
}




















