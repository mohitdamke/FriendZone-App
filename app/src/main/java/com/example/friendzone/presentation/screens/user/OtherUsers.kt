package com.example.friendzone.presentation.screens.user

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.common.PostItem
import com.example.friendzone.dimension.FontDim
import com.example.friendzone.dimension.TextDim
import com.example.friendzone.ui.theme.DarkBlack
import com.example.friendzone.ui.theme.White
import com.example.friendzone.viewmodel.auth.AuthViewModel
import com.example.friendzone.viewmodel.home.HomeViewModel
import com.example.friendzone.viewmodel.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
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


    Scaffold(
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
                        text = users?.name?.toUpperCase(Locale.ROOT) ?: "Loading",
                        maxLines = 1,
                        letterSpacing = 1.sp, fontSize = TextDim.titleTextSize,
                        overflow = TextOverflow.Visible,
                        fontFamily = FontDim.Bold,
                    )
                },
                navigationIcon = {
                    Icon(imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = modifier
                            .size(30.dp)
                            .clickable {
                                navController.navigateUp()
                            })
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBlack)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn {
                item {

                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(10.dp), verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

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
                        Text(
                            text = users?.name ?: "Loading",
                            fontSize = TextDim.titleTextSize,
                            fontFamily = FontDim.Bold,
                            color = White
                        )
                        Spacer(modifier = modifier.padding(top = 4.dp))
                        Text(
                            text = "@${users?.userName ?: "Loading Username"}",
                            fontSize = TextDim.secondaryTextSize,
                            fontFamily = FontDim.Normal,
                            color = Color.LightGray
                        )

                        Spacer(modifier = modifier.padding(top = 4.dp))
                        Text(
                            text = "@${users?.bio ?: "Loading Bio"}",
                            fontSize = TextDim.bodyTextSize,
                            fontFamily = FontDim.Normal,
                            color = Color.LightGray, textAlign = TextAlign.Center
                        )
                    }
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
                                fontSize = TextDim.titleTextSize, fontFamily = FontDim.Bold,
                                color = White
                            )
                            Text(
                                text = "posts",
                                fontSize = TextDim.secondaryTextSize, fontFamily = FontDim.Normal,
                                color = White
                            )
                        }
                        Column(
                            modifier = modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = followerList?.size.toString(),
                                fontSize = TextDim.titleTextSize, fontFamily = FontDim.Bold,
                                color = White
                            )
                            Text(
                                text = "followers",
                                fontSize = TextDim.secondaryTextSize, fontFamily = FontDim.Normal,
                                color = White
                            )
                        }
                        Column(
                            modifier = modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = followingList?.size.toString(),
                                fontSize = TextDim.titleTextSize, fontFamily = FontDim.Bold,
                                color = White
                            )
                            Text(
                                text = "following",
                                fontSize = TextDim.secondaryTextSize, fontFamily = FontDim.Normal,
                                color = White
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
                        }, elevation = ButtonDefaults.buttonElevation(10.dp),
                        border = ButtonDefaults.outlinedButtonBorder,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clip(RoundedCornerShape(40.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkBlack, contentColor = Color.White
                        )
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
}