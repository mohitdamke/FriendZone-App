package com.example.friendzone.presentation.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.friendzone.common.ChatUserItem
import com.example.friendzone.nav.routes.ChatRouteScreen
import com.example.friendzone.ui.theme.Blue40
import com.example.friendzone.ui.theme.Blue80
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
            FloatingActionButton(containerColor = Blue80, onClick = {
                navController.navigate(ChatRouteScreen.AiChat.route)
            }) {
                Icon(imageVector = Icons.Rounded.AcUnit, contentDescription = null)
            }
        }) {

        LazyColumn(modifier = modifier.fillMaxSize()) {
            item {

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(16.dp)

                ) {
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Inbox",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center, color = Blue40
                        )
                    }
                    ChatOutlineText(
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
                        } }
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
        singleLine = true, leadingIcon = {
            Icon(
                imageVector = icons,
                contentDescription = "",
                modifier = Modifier.padding(10.dp), tint = Color.LightGray
            )
        },
        label = {
            Text(
                text = "Type your $label", fontSize = 16.sp,
                fontWeight = FontWeight.W600, color = Color.LightGray,
                fontFamily = FontFamily.SansSerif, maxLines = 1
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.LightGray,
            unfocusedBorderColor = Color.LightGray,
            focusedTextColor = Color.Gray,
            unfocusedTextColor = Color.LightGray
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        modifier = modifier.fillMaxWidth()
        , shape = RoundedCornerShape(16.dp), minLines = 1
    )
}