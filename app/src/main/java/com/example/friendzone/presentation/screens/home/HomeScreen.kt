package com.example.friendzone.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
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
import com.example.friendzone.common.UsersStoryHomeItem
import com.example.friendzone.data.model.PostModel
import com.example.friendzone.data.model.UserModel
import com.example.friendzone.nav.routes.HomeRouteScreen
import com.example.friendzone.ui.theme.Blue40
import com.example.friendzone.util.SharedPref
import com.example.friendzone.viewmodel.home.HomeViewModel
import com.example.friendzone.viewmodel.search.SearchViewModel
import com.example.friendzone.viewmodel.story.AddStoryViewModel
import com.example.friendzone.viewmodel.story.StoryViewModel
import com.example.friendzone.viewmodel.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {

    val homeViewModel: HomeViewModel = viewModel()
    val storyViewModel: StoryViewModel = viewModel()
    val addStoryViewModel: AddStoryViewModel = viewModel()
    val searchViewModel: SearchViewModel = viewModel()
    val usersList by searchViewModel.userList.observeAsState(null)
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    val userViewModel: UserViewModel = viewModel()

    val context = LocalContext.current

    val postAndUsers: List<Pair<PostModel, UserModel>> by homeViewModel.postsAndUsers.observeAsState(
        emptyList()
    )
    val storyAndUsers by storyViewModel.storyAndUsers.observeAsState(null)




    LaunchedEffect(key1 = Unit) {
        userViewModel.fetchUsers(uid = currentUserId)
        searchViewModel.fetchUsersExcludingCurrentUser(currentUserId)
    }
    Scaffold(modifier = modifier) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
        ) {
            item {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "FriendZone",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Blue40
                    )
                }

                Spacer(modifier = modifier.padding(top = 20.dp))

                Row(
                    modifier = modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    LazyRow(modifier = Modifier.padding(start = 4.dp)) {
                        item {
                            Column(
                                modifier = modifier,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box {
                                    Image(painter = rememberAsyncImagePainter(
                                        model = SharedPref.getImageUrl(
                                            context
                                        )
                                    ),
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clickable {
                                                if (storyAndUsers != null && storyAndUsers!!.isNotEmpty()) {
                                                    val routes =
                                                        HomeRouteScreen.AllStory.route.replace(
                                                            oldValue = "{all_story}",
                                                            newValue = currentUserId
                                                        )
                                                    navController.navigate(routes)
                                                } else {
                                                    navController.navigate(HomeRouteScreen.AddStory.route)
                                                }
                                            }
                                            .clip(CircleShape)
                                            .border(
                                                width = 4.dp,
                                                color = Blue40,
                                                shape = CircleShape
                                            ), contentDescription = null,
                                        contentScale = ContentScale.Crop)
                                    Icon(imageVector = Icons.Default.AddCircle,
                                        contentDescription = null,
                                        modifier = modifier
                                            .size(22.dp)
                                            .clickable {
                                                navController.navigate(HomeRouteScreen.AddStory.route)
                                            }
                                            .align(Alignment.BottomEnd))

                                }
                                Text(
                                    text = "You",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        item {
                            Spacer(modifier = modifier.padding(start = 10.dp))
                        }
                        item {
                            if (usersList != null && usersList!!.isNotEmpty()) {
                                val filterItems = usersList!!.filter {
                                    it.uid != FirebaseAuth.getInstance().currentUser!!.uid
                                }
                                this@LazyRow.items(filterItems) { pairs ->
                                    UsersStoryHomeItem(
                                        users = pairs,
                                        navHostController = navController,
                                    )
                                }
                            }
                        }
                    }
                }

                this@LazyColumn.items(postAndUsers?: emptyList()) { pairs ->
                    PostItem(
                        post = pairs.first,
                        users = pairs.second,
                        navController = navController,
                        homeViewModel = homeViewModel  // Pass the ViewModel here
                    )
                }
            }
        }
    }
}



