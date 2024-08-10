package com.example.friendzone.presentation.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.friendzone.common.ChatUserItem
import com.example.friendzone.common.OutlineText
import com.example.friendzone.nav.routes.ChatRouteScreen
import com.example.friendzone.viewmodel.search.SearchViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ChatScreen(modifier: Modifier = Modifier, navController: NavController) {

    val searchViewModel: SearchViewModel = viewModel()
    val context = LocalContext.current
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    var searchChat by remember { mutableStateOf("") }

    val usersList by searchViewModel.userList.observeAsState(null)
    LaunchedEffect(Unit) {
        searchViewModel.fetchUsersExcludingCurrentUser(currentUserId)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(ChatRouteScreen.AiChat.route)
            }, modifier = modifier.padding(10.dp)) {
                Icon(imageVector = Icons.Rounded.AcUnit, contentDescription = null)
            }
        }) {

        LazyColumn(modifier = modifier.fillMaxSize()) {
            item {

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .padding(it)
                ) {
                    Text(text = "Chat Screen")
                    OutlineText(
                        value = searchChat,
                        onValueChange = { searchChat = it },
                        label = "Chat",
                        icons = Icons.Default.Search
                    )
                    if (usersList != null && usersList!!.isNotEmpty()) {
                        val filterItems =
                            usersList!!.filter { it.name.contains(searchChat, ignoreCase = true) }

                        this@LazyColumn.items(filterItems) { pairs ->
                            ChatUserItem(
                                users = pairs,
                                navController = navController,
                            )
                        }
                    }
                }

            }
        }
    }
}