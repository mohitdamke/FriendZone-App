package com.example.friendzone.presentation.screens.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.friendzone.common.PostItem
import com.example.friendzone.ui.theme.Blue40
import com.example.friendzone.viewmodel.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

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
                            text = "Your saved posts",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Blue40
                        )
                    }
                    Spacer(modifier = modifier.padding(top = 20.dp))
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
