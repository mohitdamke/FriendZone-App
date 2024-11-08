package com.example.friendzone.presentation.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.friendzone.dimension.FontDim
import com.example.friendzone.dimension.TextDim
import com.example.friendzone.nav.routes.AuthRouteScreen
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.ui.theme.DarkBlack
import com.example.friendzone.ui.theme.SocialPink
import com.example.friendzone.ui.theme.brushAddPost
import com.example.friendzone.viewmodel.auth.SignInViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {

    val state = viewModel.signInState.collectAsState(initial = null)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(DarkBlack)
                .padding(paddingValues)
                .padding(10.dp),
        ) {
            LazyColumn {
                item {
                    Spacer(modifier = Modifier.padding(top = 70.dp))
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "FriendZone",
                            fontSize = 34.sp,
                            color = White,
                            fontFamily = FontDim.Bold,
                        )
                        Text(
                            text = "Make Social Network",
                            fontSize = 16.sp,
                            color = White,
                            fontFamily = FontDim.Normal
                        )

                        Spacer(modifier = Modifier.padding(top = 60.dp))

                        Text(
                            text = "Log in",
                            fontSize = 30.sp,
                            fontFamily = FontDim.Bold,
                            color = White
                        )
                    }
                    Spacer(modifier = Modifier.padding(top = 16.dp))

                    Text(
                        text = "Email",
                        fontSize = TextDim.titleTextSize,
                        fontFamily = FontDim.Bold,
                        color = White,
                        modifier = modifier.padding(start = 8.dp)
                    )
                    OutlineText(
                        value = email, onValueChange = { email = it },
                        label = "Email",
                        icons = Icons.Default.Email
                    )

                    Spacer(modifier = Modifier.padding(top = 16.dp))

                    Text(
                        text = "Password",
                        fontSize = TextDim.titleTextSize,
                        fontFamily = FontDim.Bold,
                        color = White,
                        modifier = modifier.padding(start = 8.dp)
                    )
                    PassOutlineText(
                        value = password, onValueChange = { password = it },
                        label = "Password",
                        icons = Icons.Default.Lock
                    )

                    Spacer(modifier = Modifier.padding(top = 40.dp))

                    TextButton(
                        onClick = {
                            if (email.isEmpty() || password.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Please fill all fields",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                return@TextButton
                            } else {
                                viewModel.loginUser(
                                    email = email,
                                    password = password,
                                    context = context
                                )
                            }
                        }, modifier = modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(brushAddPost),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Log in",
                            fontSize = TextDim.titleTextSize,
                            fontFamily = FontDim.Bold,
                            color = com.example.friendzone.ui.theme.White
                        )
                    }

                    if (state.value?.isLoading == true) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    Spacer(modifier = Modifier.padding(top = 100.dp))

                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Don't have an account?", fontSize = TextDim.titleTextSize,
                            fontFamily = FontDim.Bold,
                            color = White,
                        )
                        Spacer(modifier = Modifier.padding(start = 4.dp))
                        Text(
                            text = "Sign up",
                            fontSize = TextDim.titleTextSize,
                            fontFamily = FontDim.Bold,
                            color = SocialPink,
                            modifier = modifier
                                .padding(start = 4.dp)
                                .clickable {
                                    navController.navigate(AuthRouteScreen.Register.route)
                                },
                        )

                        Spacer(modifier = Modifier.padding(top = 16.dp))
                    }

                    LaunchedEffect(key1 = state.value?.isSuccess) {
                        scope.launch {
                            if (state.value?.isSuccess?.isNotEmpty() == true) {
                                val success = state.value?.isSuccess
                                Toast.makeText(
                                    context,
                                    "You have successfully login",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                navController.navigate(Graph.MainScreenGraph) {
                                    popUpTo(Graph.AuthGraph) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }

                    LaunchedEffect(key1 = state.value?.isError) {
                        scope.launch {
                            if (state.value?.isError?.isNotEmpty() == true) {
                                val error = state.value?.isError
                                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OutlineText(
    modifier: Modifier = Modifier,
    value: String,
    icons: ImageVector,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true, leadingIcon = {
            Icon(
                imageVector = icons,
                contentDescription = "",
                modifier = Modifier.padding(10.dp), tint = White
            )
        },
        placeholder = {
            Text(
                text = "Enter your $label",
                fontSize = TextDim.secondaryTextSize,
                fontFamily = FontDim.Medium,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Visible
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedPlaceholderColor = Color.Gray,
            focusedPlaceholderColor = Color.Gray,
            disabledContainerColor = Color.Gray,
            focusedBorderColor = Gray,
            unfocusedBorderColor = Gray,
            focusedContainerColor = DarkBlack,
            unfocusedContainerColor = DarkBlack,
            focusedTextColor = com.example.friendzone.ui.theme.White,
            unfocusedTextColor = com.example.friendzone.ui.theme.White
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(100.dp),
        minLines = 1
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PassOutlineText(
    modifier: Modifier = Modifier,
    value: String,
    icons: ImageVector,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true, leadingIcon = {
            Icon(
                imageVector = icons,
                contentDescription = "",
                modifier = Modifier.padding(10.dp), tint = White
            )
        },
        placeholder = {
            Text(
                text = "Enter your $label",
                fontSize = TextDim.secondaryTextSize,
                fontFamily = FontDim.Medium,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Visible
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedPlaceholderColor = Color.Gray,
            focusedPlaceholderColor = Color.Gray,
            disabledContainerColor = Color.Gray,
            focusedBorderColor = Gray,
            unfocusedBorderColor = Gray,
            focusedContainerColor = DarkBlack,
            unfocusedContainerColor = DarkBlack,
            focusedTextColor = com.example.friendzone.ui.theme.White,
            unfocusedTextColor = com.example.friendzone.ui.theme.White
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(100.dp),
        minLines = 1
    )
}

