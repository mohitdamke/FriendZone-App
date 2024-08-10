package com.example.friendzone.presentation.screens.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.friendzone.common.PostItem
import com.example.friendzone.viewmodel.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Saved(modifier: Modifier = Modifier, navController: NavController) {
    val homeViewModel: HomeViewModel = viewModel()
    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    val savedPosts by homeViewModel.fetchSavedPost(userId).observeAsState(emptyList())

    LazyColumn {
        item {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Saved")
                savedPosts.forEach { post ->
                    val user by homeViewModel.getUserById(post.userId).observeAsState(null)
                    user?.let {
                        PostItem(
                            post = post,
                            users = it,
                            navController = navController,
//                            userId = userId,

                            )
                    }
                }
            }
        }
    }
}