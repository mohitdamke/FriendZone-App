package com.example.friendzone.presentation.screens.story

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.common.DetailStoryItem
import com.example.friendzone.dimension.FontDim
import com.example.friendzone.dimension.TextDim
import com.example.friendzone.ui.theme.DarkBlack
import com.example.friendzone.ui.theme.White
import com.example.friendzone.viewmodel.story.AddStoryViewModel
import com.example.friendzone.viewmodel.story.StoryViewModel
import com.example.friendzone.viewmodel.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AllStory(
    modifier: Modifier = Modifier,
    navController: NavController,
    uid: String
) {
    val userViewModel: UserViewModel = viewModel()
    val storyViewModel: StoryViewModel = viewModel()
    val addStoryViewModel: AddStoryViewModel = viewModel()

    val users by userViewModel.users.observeAsState(null)
    val story by userViewModel.story.observeAsState(emptyList())
    val deleteSuccess by userViewModel.deleteSuccess.observeAsState(false)
    val currentUser by userViewModel.currentUser.observeAsState(null)
    val context = LocalContext.current

    LaunchedEffect(key1 = uid) {
        userViewModel.fetchStory(uid)
        userViewModel.fetchUsers(uid)
        userViewModel.fetchUserDetails(uid)  // This should fetch details for the clicked user.
        userViewModel.fetchUsers(FirebaseAuth.getInstance().currentUser!!.uid)
    }

    LaunchedEffect(key1 = FirebaseAuth.getInstance().currentUser!!.uid) {
        userViewModel.fetchUserDetails(FirebaseAuth.getInstance().currentUser!!.uid)  // This fetches details for the current user.
    }

    LaunchedEffect(deleteSuccess) {
        if (deleteSuccess) {
            Toast.makeText(
                context,
                "Story has been successfully deleted",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val pagerState = rememberPagerState(pageCount = { story.size })

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkBlack,
                    titleContentColor = White,
                    actionIconContentColor = White,
                    navigationIconContentColor = White,
                    scrolledContainerColor = DarkBlack,
                ),
                title = {
                    Text(
                        "Story", maxLines = 1,
                        letterSpacing = 1.sp, fontSize = TextDim.titleTextSize,
                        overflow = TextOverflow.Visible,
                        fontFamily = FontDim.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = modifier.size(28.dp)

                        )
                    }
                },
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        if (currentUser != null) {
            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(DarkBlack)
                        .padding(paddingValues)
                        .padding(10.dp),
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val imageUrl = users?.imageUrl
                        Image(
                            painter = rememberAsyncImagePainter(model = imageUrl),
                            contentDescription = null,
                            modifier = modifier
                                .size(60.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = modifier.padding(start = 10.dp))
                        Text(
                            text = users?.name ?: "",
                            fontSize = TextDim.titleTextSize,
                            fontFamily = FontDim.Bold,
                            color = White
                        )
                    }

                    if (story.isEmpty()) {
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
                            modifier = Modifier.fillMaxSize()
                        ) { pageIndex ->
                            val storyDetail = story.getOrNull(pageIndex)
                            if (storyDetail != null) {
                                DetailStoryItem(
                                    story = storyDetail,
                                    users = users ?: return@HorizontalPager,
                                    onDelete = {
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
