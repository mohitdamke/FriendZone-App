package com.example.friendzone.presentation.screens.user

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.common.PostItem
import com.example.friendzone.nav.routes.HomeRouteScreen
import com.example.friendzone.nav.routes.MainRouteScreen
import com.example.friendzone.viewmodel.auth.AuthViewModel
import com.example.friendzone.viewmodel.home.HomeViewModel
import com.example.friendzone.viewmodel.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OtherUsers(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    uid: String,
    context: Context = LocalContext.current
) {

    var currentUserId = ""

    if (FirebaseAuth.getInstance().currentUser != null) {
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    }

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val userViewModel: UserViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val posts by userViewModel.posts.observeAsState(null)
    val story by userViewModel.story.observeAsState(null)
    val users by userViewModel.users.observeAsState(null)
    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)
    val isFollowing = followerList?.contains(currentUserId) == true

    LaunchedEffect(key1 = uid) {
        userViewModel.fetchPosts(uid)
        userViewModel.fetchStory(uid)
        userViewModel.fetchUsers(uid)
        userViewModel.getFollowers(uid)
        userViewModel.getFollowing(uid)
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            item {
                Text(text = "User Profile", fontSize = 30.sp, fontWeight = FontWeight.SemiBold)

                Spacer(modifier = modifier.padding(10.dp))
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.padding(10.dp))
                    Row(
                        modifier = modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = modifier
                                .weight(0.7f)
                        ) {
                            Text(text = users!!.name, fontSize = 26.sp)
                            Spacer(modifier = modifier.padding(4.dp))
                            Text(text = "@${users!!.userName}", fontSize = 16.sp)
                            Spacer(modifier = modifier.padding(4.dp))
                            Text(
                                text = users!!.bio,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = modifier.padding(4.dp))

                            Text(text = "${followerList?.size} Followers", fontSize = 18.sp)
                            Spacer(modifier = modifier.padding(4.dp))

                            Text(text = "${followingList?.size} Following", fontSize = 18.sp)
                            Spacer(modifier = modifier.padding(top = 8.dp))
                        }
                        if (uid == currentUserId) {
                            Button(
                                onClick = {
                                }, modifier = modifier.weight(0.3f)
                            ) {
                                Text(text = "Edit Profile")
                            }
                        } else {
                            Button(
                                onClick = {
                                    if (currentUserId != "") {
                                        userViewModel.followOrUnfollowUser(
                                            uid,
                                            currentUserId,
                                            isFollowing
                                        )
                                    }
                                }, modifier = modifier.weight(0.3f)
                            ) {
                                Text(
                                    text = if (isFollowing) {
                                        "Following"
                                    } else {
                                        "Follow"
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = modifier.padding(16.dp))

                    Image(
                        painter = rememberAsyncImagePainter(
                            model = users!!.imageUrl
                        ),
                        contentDescription = null,
                        modifier = modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (story != null && story!!.isEmpty()) {
                                    navController.navigate(HomeRouteScreen.AllStory.route) {
                                        popUpTo(MainRouteScreen.Profile.route) {
                                            inclusive = true
                                        }
                                    }
                                }

                            }
                    )
                }
                Spacer(modifier = Modifier.padding(16.dp))

            }
            if (posts != null && users != null) {
                this@LazyColumn.items(
                    posts ?: emptyList()
                ) { post ->
                    val isSaved = false
                    PostItem(
                        post = post,
                        users = users!!,
                        navController = navController,
//                        userId = SharedPref.getUserName(context),
                    )
                }
            }
        }
    }
}
