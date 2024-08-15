package com.example.friendzone.presentation.screens.story

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.common.DetailStoryItem
import com.example.friendzone.data.model.StoryModel
import com.example.friendzone.viewmodel.story.AddStoryViewModel
import com.example.friendzone.viewmodel.story.StoryViewModel
import com.example.friendzone.viewmodel.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllStory(
    modifier: Modifier = Modifier,
    navController: NavController,
    uid: String
) {

    val userViewModel: UserViewModel = viewModel()

    val users by userViewModel.users.observeAsState(null)
    val story by userViewModel.story.observeAsState(null)

    val storyViewModel: StoryViewModel = viewModel()
    val addStoryViewModel: AddStoryViewModel = viewModel()
    val deleteSuccess by userViewModel.deleteSuccess.observeAsState(false)
    val context = LocalContext.current

    val storyAndUsers by storyViewModel.storyAndUsers.observeAsState(null)
    val pagerState = rememberPagerState(pageCount = {
        story?.size ?: 0
    })
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
//    val uidStory = story?.get(pagerState.currentPage)?.uidStory
    val storyModel = StoryModel()

    LaunchedEffect(key1 = uid) {
        userViewModel.fetchStory(uid)
        userViewModel.fetchUsers(uid)
    }


    LaunchedEffect(deleteSuccess) {
        if (deleteSuccess) {
            Toast.makeText(
                context,
                "Story have been Successfully Deleted",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    Scaffold { paddingValues ->

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {

                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp),
                    ) {
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = users!!.imageUrl),
                                contentDescription = null, modifier = modifier
                                    .size(40.dp)
                                    .clip(CircleShape), contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = modifier.padding(start = 10.dp))
                            Text(text = users?.name ?: "", fontSize = 18.sp)


                        }
                        if (story == null) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Loading stories...",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        } else if (story!!.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No stories found.",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        } else {
                            Spacer(modifier = modifier.height(20.dp))
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxSize()
                            ) { it ->
                                story ?: emptyList()
                                val storyDetail = story!![it]
                                DetailStoryItem(
                                    story = storyDetail!!,
                                    users = users!!,
                                    onDelete = {
                                        userViewModel.fetchStory(uid)
                                        userViewModel.deleteStory(storyKey = storyDetail.storyKey)
                                        userViewModel.fetchStory(uid)
                                    }
                                )

                            }
                        }
                    }
                }
            }
        }
    }
}
