package com.example.friendzone.presentation.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.data.model.ChatModel
import com.example.friendzone.dimension.FontDim
import com.example.friendzone.dimension.TextDim
import com.example.friendzone.nav.routes.SearchRouteScreen
import com.example.friendzone.ui.theme.DarkBlack
import com.example.friendzone.ui.theme.SocialBlue
import com.example.friendzone.ui.theme.White
import com.example.friendzone.ui.theme.brushAddPost
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
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = DarkBlack,
                titleContentColor = White,
                actionIconContentColor = White,
                navigationIconContentColor = White,
                scrolledContainerColor = DarkBlack,
            ),
            title = {
                Row(
                    modifier = modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {

                    Image(
                        painter = rememberAsyncImagePainter(model = users?.imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .clickable {
                                val routes = SearchRouteScreen.OtherProfile.route.replace(
                                    "{data}",
                                    users!!.uid
                                )
                                navController.navigate(routes)
                            }, contentScale = ContentScale.Crop
                    )

                    users?.let {
                        Text(
                            text = it.name,
                            fontSize = TextDim.titleTextSize,
                            fontFamily = FontDim.Normal,
                            color = White, modifier = modifier.padding(start = 6.dp)
                        )
                    }
                }


            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = modifier
                        .size(30.dp)
                        .clickable {
                            navController.navigateUp()

                        }
                )


            },


            )
    }, bottomBar = {
        ChatUserOutlineText(
            modifier = modifier,
            value = chatViewModel.currentMessage,
            label = "Type a message",
            onValueChange = {
                chatViewModel.currentMessage = it
            },
            onSendClick = {
                val newMessageRef = userViewModel.chatRef.child(chatId).push()
                val storeKey = newMessageRef.key ?: return@ChatUserOutlineText
                val message = ChatModel(
                    senderId = currentUserId,
                    receiverId = uid,
                    messageText = chatViewModel.currentMessage,
                    storeKey = storeKey,
                    timestamp = System.currentTimeMillis()
                )
                userViewModel.sendMessage(chatId, message)
                chatViewModel.currentMessage = ""
            },
        )
    }
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(DarkBlack)
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
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 30.dp,
                        topEnd = 30.dp,
                        bottomStart = 30.dp,
                        bottomEnd = 0.dp
                    )
                )
                .background(SocialBlue)
        ) {
            Text(
                text = message.messageText,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .padding(10.dp),
                fontSize = TextDim.titleTextSize, fontFamily = FontDim.Normal,
                color = White
            )
        }
        Text(
            text = formattedTimestamp,
            fontSize = 12.sp,
            color = LightGray,
            modifier = Modifier.padding(top = 2.dp, start = 180.dp),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.padding(top = 4.dp))

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
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 30.dp,
                        bottomStart = 30.dp,
                        bottomEnd = 30.dp
                    )
                )
                .background(com.example.friendzone.ui.theme.LightGray)
        ) {
            Text(
                text = message.messageText,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .padding(10.dp),
                fontSize = TextDim.titleTextSize, fontFamily = FontDim.Normal,
                color = White
            )
        }

        Text(
            text = formattedTimestamp,
            fontSize = 12.sp,
            color = LightGray,
            modifier = Modifier.padding(top = 2.dp, start = 180.dp)
        )
        Spacer(modifier = Modifier.padding(top = 4.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatUserOutlineText(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    label: String
) {

    Column(modifier = modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            singleLine = true,
            placeholder = {
                Text(
                    text = "Type your $label here...",
                    fontSize = TextDim.secondaryTextSize,
                    fontFamily = FontDim.Medium,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
            },
            trailingIcon = {
                Box(
                    modifier = Modifier
                        .size(40.dp) // Size of the outer circle
                        .clip(CircleShape)
                        .background(brushAddPost) // Background brush for gradient or color
                        .padding(8.dp) // Padding inside the circle
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "Send",
                        modifier = modifier
                            .size(30.dp)
                            .align(Alignment.Center)
                            .clickable {
                                onSendClick()
                            }, tint = White
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedPlaceholderColor = Color.Gray,
                focusedPlaceholderColor = Color.Gray,
                disabledContainerColor = Color.Gray,
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                focusedContainerColor = DarkBlack,
                unfocusedContainerColor = DarkBlack,
                focusedTextColor = White,
                unfocusedTextColor = White
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Send
            ),
            modifier = modifier
                .background(com.example.friendzone.ui.theme.Black)
                .fillMaxWidth()
                .padding(10.dp)
                .padding(bottom = 20.dp),
            shape = RoundedCornerShape(100.dp),
            minLines = 1
        )
    }

}