package com.example.friendzone.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
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
    post: PostModel,
    users: UserModel,
    navController: NavController,
) {

    val context = LocalContext.current
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    val homeViewModel: HomeViewModel = viewModel()

    val imagePagerState = rememberPagerState(pageCount = {
        post.images.size ?: 0
    })

    val isLiked = post.likes.containsKey(currentUserId)
    val isSaved = users.savedPosts.containsKey(post.storeKey)


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = users!!.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp)
                    .clickable {
                        if (users!!.uid == currentUserId) {
                            navController.navigate(MainRouteScreen.Profile.route) {
                                launchSingleTop = true
                            }
                        } else {
                            val routes = HomeRouteScreen.OtherProfile.route.replace("{data}", users!!.uid)
                            navController.navigate(routes) {
                                launchSingleTop = true
                            }
                        }
                    },
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = users!!.name, fontSize = 20.sp)
                Text(text = post.post, fontSize = 20.sp)
            }
        }

Spacer(modifier = modifier.height(10.dp))
        if (post.images.isNotEmpty()) {
            HorizontalPager(
                state = imagePagerState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = modifier.padding(start = 60.dp))

                Image(
                    painter = rememberAsyncImagePainter(model = post.images[it]),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Crop,
                )
            }
        }


//                Image(
//                    painter = rememberAsyncImagePainter(model = post.images),
//                    contentDescription = null,
//                    modifier = modifier.size(100.dp),
//                    contentScale = ContentScale.Crop
//                )

        Spacer(modifier = modifier.padding(top = 20.dp))

        Spacer(modifier = modifier.height(10.dp))
        Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector =
                if (isLiked) Icons.Filled.HeartBroken else Icons.Outlined.Share,
                contentDescription = null,
                modifier = modifier
                    .size(30.dp)
                    .clickable {
                        if (currentUserId.isNotEmpty()) {
                            homeViewModel.toggleLike(
                                postId = post.storeKey ?: "",
                                userId = currentUserId
                            )
                        }
                    }
            )
            Spacer(modifier = modifier.padding(start = 2.dp))
            Text(text = "${post.likes.size} Likes")
            Spacer(modifier = modifier.padding(start = 10.dp))

            Icon(imageVector = Icons.Outlined.ModeComment,
                contentDescription = null,
                modifier = modifier
                    .size(30.dp)
                    .clickable {
                        navController.navigate(
                            HomeRouteScreen.CommentDetail.route.replace(
                                "{comment_detail}",
                                post.storeKey ?: ""
                            )
                        )
                    })
            Spacer(modifier = modifier.padding(start = 2.dp))
            Text(text = "${post.comments.size} Comments")
            Spacer(modifier = modifier.padding(start = 10.dp))
            Icon(
                imageVector = if (isSaved) {
                    Icons.Outlined.Save
                } else Icons.Filled.SaveAs,
                contentDescription = null,
                modifier = modifier.clickable {
                    homeViewModel.toggleSavePost(
                        postId = post.storeKey ?: "",
                        context = context
                    )


                })
            Spacer(modifier = modifier.padding(start = 2.dp))
            Text(text = "Save")
        }

        Divider(modifier = modifier.padding(10.dp))
        Spacer(modifier = modifier.height(10.dp))

    }
}




















