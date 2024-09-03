package com.example.friendzone.presentation.screens.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.common.PostItem
import com.example.friendzone.common.UsersStoryHomeItem
import com.example.friendzone.data.model.PostModel
import com.example.friendzone.data.model.StoryModel
import com.example.friendzone.data.model.UserModel
import com.example.friendzone.dimension.FontDim
import com.example.friendzone.dimension.TextDim
import com.example.friendzone.nav.routes.HomeRouteScreen
import com.example.friendzone.ui.theme.DarkBlack
import com.example.friendzone.ui.theme.White
import com.example.friendzone.ui.theme.brushAddPost
import com.example.friendzone.util.SharedPref
import com.example.friendzone.viewmodel.home.HomeViewModel
import com.example.friendzone.viewmodel.search.SearchViewModel
import com.example.friendzone.viewmodel.story.AddStoryViewModel
import com.example.friendzone.viewmodel.story.StoryViewModel
import com.example.friendzone.viewmodel.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    homeNavController: NavController
) {

    val homeViewModel: HomeViewModel = viewModel()
    val storyViewModel: StoryViewModel = viewModel()
    val addStoryViewModel: AddStoryViewModel = viewModel()
    val searchViewModel: SearchViewModel = viewModel()
    val usersList by searchViewModel.userList.observeAsState(null)
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val userViewModel: UserViewModel = viewModel()

    val context = LocalContext.current

    val postAndUsers: List<Pair<PostModel, UserModel>> by homeViewModel.postsAndUsers.observeAsState(
        emptyList()
    )
    val storyAndUsers by storyViewModel.storyAndUsers.observeAsState(null)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(Unit) {
        userViewModel.fetchPosts(currentUserId)
        userViewModel.fetchUsers(currentUserId)
        searchViewModel.fetchUsersExcludingCurrentUser(currentUserId)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBlack,
                    titleContentColor = White,
                    actionIconContentColor = White,
                    navigationIconContentColor = White,
                    scrolledContainerColor = DarkBlack,
                ),
                title = {
                    Text(
                        text = "Hello, ${SharedPref.getName(context)}",
                        maxLines = 1,
                        letterSpacing = 1.sp,
                        fontSize = TextDim.titleTextSize,
                        overflow = TextOverflow.Visible,
                        fontFamily = FontDim.Bold,
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkBlack),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = modifier.padding(top = 10.dp))
            if (usersList.isNullOrEmpty()) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No Users Found", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                LazyColumn {
                    item {
                        StoryItem(
                            navController = navController,
                            usersList = usersList,
                            storyAndUsers = storyAndUsers,
                            currentUserId = currentUserId,
                            context = context
                        )
                    }
                    item {
                        Divider(modifier = modifier.padding(top = 10.dp), color = Gray)
                    }
                    items(postAndUsers) { pairs ->
                        PostItem(
                            post = pairs.first,
                            users = pairs.second,
                            navController = navController,
                            navController2 = homeNavController,
                            homeViewModel = homeViewModel
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun StoryItem(
    modifier: Modifier = Modifier,
    navController: NavController,
    usersList: List<UserModel>?,
    storyAndUsers: List<Pair<StoryModel, UserModel>>?,
    currentUserId: String,
    context: Context
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyRow(modifier = Modifier.padding(start = 4.dp)) {
            // Current User Story Item
            item {
                UserStory(
                    modifier = modifier,
                    imageUrl = SharedPref.getImageUrl(context),
                    onClick = {
                        val route = if (storyAndUsers.isNullOrEmpty()) {
                            HomeRouteScreen.AddStory.route
                        } else {
                            HomeRouteScreen.AllStory.route.replace(
                                oldValue = "{all_story}", newValue = currentUserId
                            )
                        }
                        navController.navigate(route)
                    }
                )
            }
            // Spacer between items
            item { Spacer(modifier = Modifier.padding(start = 10.dp)) }
            // Other Users' Stories
            if (!usersList.isNullOrEmpty()) {
                items(usersList.filter { it.uid != FirebaseAuth.getInstance().currentUser?.uid }) { user ->
                    UsersStoryHomeItem(
                        users = user,
                        navHostController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun UserStory(
    modifier: Modifier = Modifier,
    imageUrl: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .border(width = 4.dp, brush = brushAddPost, shape = CircleShape)
                    .clickable { onClick() },
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = null,
                modifier = Modifier
                    .size(22.dp)
                    .align(Alignment.BottomEnd)
                    .clickable { onClick() },
                tint = LightGray
            )
        }
        Text(
            text = "You",
            fontSize = TextDim.tertiaryTextSize,
            fontFamily = FontDim.Medium,
            color = White
        )
    }
}
