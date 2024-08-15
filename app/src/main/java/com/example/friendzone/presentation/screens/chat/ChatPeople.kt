package com.example.friendzone.presentation.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.R
import com.example.friendzone.data.model.ChatModel
import com.example.friendzone.nav.routes.ChatRouteScreen
import com.example.friendzone.nav.routes.HomeRouteScreen
import com.example.friendzone.nav.routes.SearchRouteScreen
import com.example.friendzone.ui.theme.Blue40
import com.example.friendzone.ui.theme.Blue80
import com.example.friendzone.ui.theme.PurpleGrey80
import com.example.friendzone.viewmodel.chat.PeopleChatViewModel
import com.example.friendzone.viewmodel.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPeople(
    modifier: Modifier = Modifier,
    navController: NavController,
    uid: String,
    chatViewModel: PeopleChatViewModel = viewModel()
) {
    val chatState by chatViewModel.chatState.collectAsState()
    var currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    if (FirebaseAuth.getInstance().currentUser != null) {
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    }


    val userViewModel: UserViewModel = viewModel()
    val users by userViewModel.users.observeAsState(null)
    val chatMessages by userViewModel.chatMessages.observeAsState(listOf())


    val chatId = if (currentUserId < uid) "$currentUserId-$uid" else "$uid-$currentUserId"

    LaunchedEffect(key1 = uid) {
        userViewModel.fetchUsers(uid)
        chatViewModel.fetchMessages(chatId) // Fetch messages using chatId
        userViewModel.fetchChatMessages(chatId) // Fetch chat messages
    }
    Scaffold(topBar = {
        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ), title = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = modifier.padding(start = 20.dp))

                Image(
                    painter = rememberAsyncImagePainter(model = users?.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .clickable {
                            val routes = SearchRouteScreen.OtherProfile.route.replace(
                                "{data}",
                                users!!.uid
                            )
                            navController.navigate(routes)
                        }
                        .border(
                            width = 2.dp,
                            color = Blue40,
                            shape = CircleShape
                        )
                        .size(40.dp), contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    users?.let { Text(text = it.name, fontSize = 20.sp, color = Blue40) }
                }
            }


        }, navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = modifier
                    .size(30.dp)
                    .clickable {
                        navController.navigateUp()

                    }, tint = Blue40
            )


        })
    }) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                // Chat Messages Section
//        val messageList = chatState.messages.values.toList()
                val messageList = chatMessages

                LazyColumn(
                    modifier = modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Bottom // Scroll to the bottom
                ) {
                    items(messageList) { message ->
                        if (message.senderId == currentUserId) {
                            UserMessageItem(
                                message = message,
                                onDelete = {
                                    userViewModel.deleteMessage(
                                        chatId = chatId,
                                        storeKey = message.storeKey
                                    )
                                }
                            )
                        } else {
                            OtherMessageItem(
                                message = message,
                                onDelete = {
                                    userViewModel.deleteMessage(
                                        chatId = chatId,
                                        storeKey = message.storeKey
                                    )
                                }
                            )
                        }
                    }
                }

                // Input Field Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    OutlinedTextField(
                        value = chatViewModel.currentMessage,
                        onValueChange = { chatViewModel.currentMessage = it },
                        label = {
                            Text(
                                text = "Type your message", fontSize = 16.sp,
                                fontWeight = FontWeight.W600, color = Blue40,
                                fontFamily = FontFamily.SansSerif, maxLines = 1
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Blue40,
                            unfocusedBorderColor = Blue40,
                            focusedTextColor = Blue40,
                            unfocusedTextColor = Blue40
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Send
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f), minLines = 1
                    )

                    IconButton(onClick = {
                        val newMessageRef = userViewModel.chatRef.child(chatId).push()
                        val storeKey = newMessageRef.key ?: return@IconButton
                        val message = ChatModel(
                            senderId = currentUserId,
                            receiverId = uid,
                            messageText = chatViewModel.currentMessage,
                            storeKey = storeKey,
                            timestamp = System.currentTimeMillis()
                        )
                        userViewModel.sendMessage(chatId, message)
                        chatViewModel.currentMessage = ""
                    }) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            modifier = modifier.size(30.dp), tint = Blue40
                        )
                    }
                }

            }
        }

    }
}

private fun formatTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault()) // Adjust format as needed
    return dateFormat.format(timestamp)
}

@Composable
fun UserMessageItem(message: ChatModel, onDelete: (String) -> Unit) {
    val formattedTimestamp = formatTimestamp(message.timestamp)
    Column(
        modifier = Modifier
            .padding(start = 100.dp, bottom = 2.dp)
            .clickable { onDelete(message.storeKey) }
    ) {
        Text(
            text = message.messageText,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Blue40)
                .padding(16.dp),
            color = Black
        )
        Text(
            text = formattedTimestamp,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 2.dp, start = 180.dp),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun OtherMessageItem(message: ChatModel, onDelete: (String) -> Unit) {
    val formattedTimestamp = formatTimestamp(message.timestamp)

    Column(
        modifier = Modifier
            .padding(end = 100.dp, bottom = 2.dp)
            .clickable { onDelete(message.storeKey) }
    ) {
        Text(
            text = message.messageText,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Blue80)
                .padding(16.dp),
            color = Black
        )
        Text(
            text = formattedTimestamp,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 2.dp, start = 180.dp)
        )
    }
}
