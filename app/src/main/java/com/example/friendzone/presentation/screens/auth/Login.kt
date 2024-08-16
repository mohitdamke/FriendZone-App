package com.example.friendzone.presentation.screens.auth

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.friendzone.nav.routes.AuthRouteScreen
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.ui.theme.Blue40
import com.example.friendzone.viewmodel.auth.SignInViewModel
import kotlinx.coroutines.launch

@Composable
fun Login(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {

    val state = viewModel.signInState.collectAsState(initial = null)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = modifier
            .background(Blue40)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(top = 100.dp))
            Text(
                text = "FriendZone",
                fontSize = 34.sp,
                color = White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Make Social Network",
                fontSize = 16.sp,
                color = White,
                fontWeight = FontWeight.Thin
            )
            Spacer(modifier = Modifier.padding(top = 100.dp))


            Card(
                modifier = modifier,
                shape = RoundedCornerShape(30.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(White)
            ) {
                Column(modifier = modifier.padding(20.dp)) {

                    Spacer(modifier = Modifier.padding(top = 30.dp))
                    Text(text = "Log in", fontSize = 34.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.padding(top = 30.dp))

                    Text(
                        text = "Email",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = modifier.padding(start = 8.dp)
                    )
                    OutlineText(
                        value = email, onValueChange = { email = it },
                        label = "Email",
                        icons = Icons.Default.Email
                    )

                    Spacer(modifier = Modifier.padding(top = 30.dp))

                    Text(
                        text = "Password",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = modifier.padding(start = 8.dp)
                    )
                    PassOutlineText(
                        value = password, onValueChange = { password = it },
                        label = "Password",
                        icons = Icons.Default.Lock
                    )

                    Spacer(modifier = Modifier.padding(top = 40.dp))

                    Button(
                        onClick = {
                            if (email.isEmpty() || password.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Please fill all fields",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                return@Button
                            } else {
                                viewModel.loginUser(
                                    email = email,
                                    password = password,
                                    context = context
                                )
                            }
                        }, modifier = modifier.fillMaxWidth(), colors = ButtonColors(
                            contentColor = White,
                            containerColor = Blue40,
                            disabledContentColor = Gray,
                            disabledContainerColor = Blue40
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Log in",
                            fontSize = 16.sp,
                            modifier = modifier.padding(10.dp)
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
                        Text(text = "Don't have an account?", fontWeight = FontWeight.Medium)
                        Text(
                            text = "Sign up", fontWeight = FontWeight.ExtraBold,
                            modifier = modifier
                                .padding(start = 4.dp)
                                .clickable {
                                    navController.navigate(AuthRouteScreen.Register.route)
                                },
                            color = Blue40
                        )
                    }
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
                modifier = Modifier.padding(10.dp), tint = Black
            )
        },
        label = {
            Text(
                text = "Enter your $label", fontSize = 16.sp,
                fontWeight = FontWeight.W600, color = Black,
                fontFamily = FontFamily.SansSerif, maxLines = 1
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Black,
            unfocusedBorderColor = Black,
            focusedTextColor = Black,
            unfocusedTextColor = Black
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), minLines = 1
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
                modifier = Modifier.padding(10.dp), tint = Black
            )
        },
        label = {
            Text(
                text = "Enter your $label", fontSize = 16.sp,
                fontWeight = FontWeight.W600, color = Black,
                fontFamily = FontFamily.SansSerif, maxLines = 1
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Black,
            unfocusedBorderColor = Black,
            focusedTextColor = Black,
            unfocusedTextColor = Black
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        visualTransformation = PasswordVisualTransformation(),

        modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), minLines = 1
    )
}

