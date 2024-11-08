package com.example.friendzone.presentation.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.friendzone.common.ChatUserItem
import com.example.friendzone.dimension.FontDim
import com.example.friendzone.dimension.TextDim
import com.example.friendzone.nav.routes.ChatRouteScreen
import com.example.friendzone.ui.theme.Blue80
import com.example.friendzone.ui.theme.DarkBlack
import com.example.friendzone.ui.theme.White
import com.example.friendzone.ui.theme.brushAddPost
import com.example.friendzone.viewmodel.search.SearchViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(modifier: Modifier = Modifier, navController: NavController) {

    val searchViewModel: SearchViewModel = viewModel()
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    var searchChat by remember { mutableStateOf("") }

    val usersList by searchViewModel.userList.observeAsState(null)

    LaunchedEffect(Unit) {
        searchViewModel.fetchUsersExcludingCurrentUser(currentUserId)
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
                        "MESSAGES", maxLines = 1,
                        letterSpacing = 1.sp, fontSize = TextDim.titleTextSize,
                        overflow = TextOverflow.Visible,
                        fontFamily = FontDim.Bold,
                    )
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(containerColor = Blue80, modifier = modifier
                .size(60.dp)
                .rotate(270f), onClick = {
                navController.navigate(ChatRouteScreen.AiChat.route)
            }) {
                Box(
                    modifier = Modifier
                        .size(60.dp) // Size of the outer circle
                        .background(brushAddPost) // Background brush for gradient or color
                        .padding(8.dp) // Padding inside the circle
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AcUnit,
                        contentDescription = "Ai",
                        modifier = modifier
                            .size(30.dp)
                            .align(Alignment.Center), tint = White
                    )
                }
            }
        }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(DarkBlack)
                .padding(paddingValues)
                .padding(10.dp)
        ) {

            ChatOutlineText(
                value = searchChat,
                onValueChange = { searchChat = it },
                label = "Chat",
                icons = Icons.Default.Search
            )
            LazyColumn(modifier = modifier) {
                if (usersList != null && usersList!!.isNotEmpty()) {
                    val filterItems =
                        usersList!!.filter { it.name.contains(searchChat, ignoreCase = true) }

                    items(filterItems) { pairs ->
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatOutlineText(
    modifier: Modifier = Modifier,
    value: String,
    icons: ImageVector,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value.trim(),
        onValueChange = { onValueChange(it) },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = icons,
                contentDescription = "",
                modifier = Modifier
                    .size(30.dp),
                tint = Color.LightGray

            )
        },
        placeholder = {
            Text(
                text = "Type your $label",
                fontSize = TextDim.secondaryTextSize,
                fontFamily = FontDim.Medium,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Visible
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedPlaceholderColor = Color.Gray,
            focusedPlaceholderColor = Color.Gray,
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = White,
            unfocusedTextColor = White
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
        ),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(100.dp),
        minLines = 1
    )
}