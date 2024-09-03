package com.example.friendzone.presentation.screens.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.friendzone.common.PostItem
import com.example.friendzone.dimension.FontDim
import com.example.friendzone.dimension.TextDim
import com.example.friendzone.ui.theme.DarkBlack
import com.example.friendzone.ui.theme.White
import com.example.friendzone.viewmodel.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    homeNavController: NavController
) {
    val homeViewModel: HomeViewModel = viewModel()
    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    // Fetch saved posts from the ViewModel
    val savedPosts by homeViewModel.fetchSavedPost(userId).observeAsState(emptyList())

    LaunchedEffect(userId) {
        homeViewModel.fetchSavedPost(userId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = DarkBlack,
                titleContentColor = White,
                actionIconContentColor = White,
                navigationIconContentColor = White,
                scrolledContainerColor = DarkBlack,
            ), title = {
                Text(
                    text = "SAVED",
                    maxLines = 1,
                    letterSpacing = 1.sp, fontSize = TextDim.titleTextSize,
                    overflow = TextOverflow.Visible,
                    fontFamily = FontDim.Bold,
                )
            }, navigationIcon = {
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
                .padding(paddingValues)
        ) {
            LazyColumn {
                item {
                    savedPosts.forEach { post ->
                        val user by homeViewModel.getUserById(post.userId).observeAsState(null)
                        user?.let {
                            PostItem(
                                post = post,
                                users = it,
                                navController = navController,
                                homeViewModel = homeViewModel,
                                navController2 = homeNavController
                            )
                        }
                    }
                }
            }
        }
    }

}