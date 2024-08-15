package com.example.friendzone.presentation.screens.profile

import android.content.Context
import android.util.Log
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
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.common.PostItem
import com.example.friendzone.data.model.UserModel
import com.example.friendzone.nav.routes.AuthRouteScreen
import com.example.friendzone.nav.routes.MainRouteScreen
import com.example.friendzone.nav.routes.ProfileRouteScreen
import com.example.friendzone.ui.theme.Blue40
import com.example.friendzone.ui.theme.Blue80
import com.example.friendzone.util.SharedPref
import com.example.friendzone.viewmodel.auth.AuthViewModel
import com.example.friendzone.viewmodel.home.HomeViewModel
import com.example.friendzone.viewmodel.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    context: Context = LocalContext.current,
) {
    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)
    val homeViewModel: HomeViewModel = viewModel()

    val userViewModel: UserViewModel = viewModel()
    val posts by userViewModel.posts.observeAsState(null)


    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)
    val scope = rememberCoroutineScope()

    if (firebaseUser != null) userViewModel.fetchPosts(firebaseUser!!.uid)

    var currentUserId = ""
    if (FirebaseAuth.getInstance().currentUser != null) currentUserId =
        FirebaseAuth.getInstance().currentUser!!.uid

    val hasFetchedData = remember { mutableStateOf(false) }

//    LaunchedEffect(key1 = currentUserId) {
//        if (currentUserId.isNotEmpty() && !hasFetchedData.value) {
//            userViewModel.getFollowers(currentUserId)
//            userViewModel.getFollowing(currentUserId)
//            hasFetchedData.value = true
//        }
//        else{
//            userViewModel.getFollowers(currentUserId)
//            userViewModel.getFollowing(currentUserId)
//        }
//    }
//


//
//    if (currentUserId != "") {
//        LaunchedEffect(key1 = currentUserId) {
//            userViewModel.getFollowers(currentUserId)
//            userViewModel.getFollowing(currentUserId)
//            userViewModel.fetchPosts(currentUserId)
//            homeViewModel.fetchSavedPost(userId = currentUserId)
//        }
//    }


    val currentUser = FirebaseAuth.getInstance().currentUser?.uid

    val savedThreadIds by homeViewModel.savedPostIds.observeAsState(emptyList())
    val postsToDisplay =
        posts!!.filter { it.userId == currentUserId } // Filter for uploaded posts
    val savedPostsToDisplay =
        postsToDisplay.filter { savedThreadIds.contains(it.userId) } // Filter for saved posts
    val unsavedPostsToDisplay =
        postsToDisplay.filter { !savedThreadIds.contains(it.userId) } // Filter for unsaved posts




    LaunchedEffect(currentUser) {
        Log.d("TAG currentUser", "Current user Logout: STARTS " + currentUser.toString())
        if (currentUser == null) {
            navController.navigate(AuthRouteScreen.Login.route) {
                popUpTo(MainRouteScreen.Profile.route) {
                    inclusive = true
                }
            }
        }
    }

    val user = UserModel(
        email = SharedPref.getEmail(context),
        name = SharedPref.getName(context),
        userName = SharedPref.getUserName(context),
        bio = SharedPref.getBio(context),
        imageUrl = SharedPref.getImageUrl(context),
    )

    Scaffold { paddingValues ->

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Your Profile",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Blue40
                        )
                    }
                    Spacer(modifier = modifier.padding(top = 20.dp))
                    Box(modifier = modifier) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = SharedPref.getImageUrl(
                                    context
                                )
                            ),
                            contentDescription = null,
                            modifier = modifier
                                .size(120.dp)
                                .padding(2.dp)
                                .border(width = 4.dp, color = White, shape = CircleShape)
                                .padding(2.dp)
                                .border(width = 4.dp, color = Black, shape = CircleShape)
                                .clip(CircleShape), contentScale = ContentScale.Crop
                        )

                        Icon(imageVector = Icons.Default.AddCircle,
                            contentDescription = null,
                            modifier = modifier
                                .clickable {
                                    navController.navigate(ProfileRouteScreen.AddStory.route)
                                }
                                .clip(CircleShape)
                                .size(34.dp)
                                .align(Alignment.BottomEnd), tint = Black
                        )
                    }

                    Text(text = user.name, fontSize = 26.sp)
                    Spacer(modifier = modifier.padding(top = 4.dp))
                    Text(text = "@${user.userName}", fontSize = 16.sp)
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Button(
                            onClick = {
                                navController.navigate(
                                    ProfileRouteScreen.EditProfile.route
                                )
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(40.dp))
                                .padding(10.dp),
                            elevation = ButtonDefaults.buttonElevation(10.dp),
                            colors = ButtonColors(
                                containerColor = Blue80,
                                contentColor = Black,
                                disabledContainerColor = Blue80,
                                disabledContentColor = Black
                            )
                        ) {
                            Text(
                                text = "Edit Profile",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium, modifier = modifier.padding(
                                    10.dp
                                )
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Setting",
                            modifier = modifier
                                .size(30.dp)
                                .clickable {
                                    navController.navigate(
                                        ProfileRouteScreen.Settings.route
                                    )
                                }
                        )
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Setting",
                            modifier = modifier
                                .size(30.dp)
                                .clickable {
                                    navController.navigate(
                                        ProfileRouteScreen.SavedPosts.route
                                    )
                                }
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

                    this@LazyColumn.items(savedPostsToDisplay + unsavedPostsToDisplay) { post ->
                        PostItem(
                            post = post,
                            users = user,
                            navController = navController,
                            homeViewModel = homeViewModel
                        )
                    }
                }
            }
        }


        LaunchedEffect(key1 = firebaseUser) {
            if (firebaseUser == null) {

                navController.navigate(
                    AuthRouteScreen.Login.route
                ) {
                    popUpTo(MainRouteScreen.Profile.route) {
                        inclusive = true
                    }
                }
            } else {
                Unit
            }
        }

    }

}