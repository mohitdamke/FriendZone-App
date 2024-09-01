package com.example.friendzone.presentation.screens.user

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.common.PostItem
import com.example.friendzone.nav.routes.ProfileRouteScreen
import com.example.friendzone.ui.theme.Blue40
import com.example.friendzone.util.SharedPref
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

    val postsToDisplay =
        posts!!.filter { it.userId == uid }

    LaunchedEffect(key1 = uid) {
        userViewModel.fetchPosts(uid)
        userViewModel.fetchStory(uid)
        userViewModel.fetchUsers(uid)
        userViewModel.getFollowers(uid)
        userViewModel.getFollowing(uid)
    }


    Scaffold { paddingValues ->

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${users?.name ?: "Loading"} Profile",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Blue40
                        )
                    }
                    Spacer(modifier = modifier.padding(top = 20.dp))
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = users?.imageUrl ?: "https://picsum.photos/200/300"
                        ),
                        contentDescription = null,
                        modifier = modifier
                            .size(120.dp)
                            .padding(2.dp)
                            .border(width = 4.dp, color = Color.White, shape = CircleShape)
                            .padding(2.dp)
                            .border(width = 4.dp, color = Color.Black, shape = CircleShape)
                            .clip(CircleShape), contentScale = ContentScale.Crop
                    )



                    Text(text = users?.name ?: "Loading", fontSize = 26.sp)
                    Spacer(modifier = modifier.padding(top = 4.dp))
                    Text(text = "@${users?.userName ?: "Loading Username"}", fontSize = 16.sp)
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(
                            modifier = modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = postsToDisplay.size.toString(),
                                fontSize = 28.sp, fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "posts",
                                fontSize = 18.sp, fontWeight = FontWeight.Normal
                            )
                        }
                        Column(
                            modifier = modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = followerList?.size.toString(),
                                fontSize = 28.sp, fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "followers",
                                fontSize = 18.sp, fontWeight = FontWeight.Normal
                            )
                        }
                        Column(
                            modifier = modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = followingList?.size.toString(),
                                fontSize = 28.sp, fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "following",
                                fontSize = 18.sp, fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (currentUserId != "") {
                                userViewModel.followOrUnfollowUser(
                                    uid,
                                    currentUserId,
                                    isFollowing
                                )
                            }
                        }, modifier = modifier.fillMaxWidth(), colors = ButtonColors(
                            contentColor = Color.White,
                            containerColor = Blue40,
                            disabledContentColor = Color.Gray,
                            disabledContainerColor = Blue40
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (isFollowing) {
                                "Following"
                            } else {
                                "Follow"
                            }, fontSize = 16.sp,
                            modifier = modifier.padding(10.dp)
                        )
                    }

                }
            }
            if (users != null) {
                this@LazyColumn.items(
                    posts ?: emptyList()
                ) { post ->
                    PostItem(
                        post = post,
                        users = users!!,
                        navController = navController,
                        homeViewModel = homeViewModel
                    )
                }
            }
        }
    }
}
