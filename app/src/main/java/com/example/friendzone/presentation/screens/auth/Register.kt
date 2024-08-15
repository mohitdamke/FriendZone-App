package com.example.friendzone.presentation.screens.auth

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.R
import com.example.friendzone.nav.routes.AuthRouteScreen
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.ui.theme.Blue40
import com.example.friendzone.viewmodel.auth.SignUpViewModel
import kotlinx.coroutines.launch

@Composable
fun Register(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {

    val state = viewModel.signUpState.collectAsState(initial = null)
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }


    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
        } else {
        }


    }
    Box(
        modifier = modifier
            .background(Blue40)
    ) {
        LazyColumn {
            item {
                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.padding(top = 100.dp))
                    Text(
                        text = "FriendZone",
                        fontSize = 34.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Make Social Network",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Thin
                    )
                    Spacer(modifier = Modifier.padding(top = 100.dp))


                    Card(
                        modifier = modifier,
                        shape = RoundedCornerShape(30.dp),
                        elevation = CardDefaults.cardElevation(10.dp),
                        colors = CardDefaults.cardColors()
                    ) {
                        Spacer(modifier = Modifier.padding(top = 30.dp))
                        Column(modifier = modifier.padding(20.dp)) {

                            Text(
                                text = "Create an account",
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Image(
                                painter = if (imageUri == null) {
                                    painterResource(id = R.drawable.man)
                                } else {
                                    rememberAsyncImagePainter(
                                        model = imageUri,
                                    )
                                },
                                contentDescription = null, modifier = modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        val isGranted = ContextCompat.checkSelfPermission(
                                            context,
                                            permissionToRequest
                                        ) == PackageManager.PERMISSION_GRANTED

                                        if (isGranted) {
                                            launcher.launch("image/*")
                                        } else {
                                            permissionLauncher.launch(permissionToRequest)
                                        }

                                    }, contentScale = ContentScale.Crop
                            )
                            if (imageUri == null) {

                                Text(text = "Upload your profile picture")
                            } else {
                                Text(text = "Change your profile picture")

                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                        ) {

                            Spacer(modifier = Modifier.padding(top = 30.dp))

                            Text(
                                text = "Full name",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = modifier.padding(start = 8.dp)
                            )
                            OutlineText(
                                value = name, onValueChange = { name = it },
                                label = "Name",
                                icons = Icons.Default.Person,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                )
                            )

                            Spacer(modifier = Modifier.padding(top = 30.dp))

                            Text(
                                text = "Username",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = modifier.padding(start = 8.dp)
                            )
                            OutlineText(
                                value = username, onValueChange = { username = it },
                                label = "Username",
                                icons = Icons.Default.Person2,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                )
                            )

                            Spacer(modifier = Modifier.padding(top = 30.dp))

                            Text(
                                text = "Bio",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = modifier.padding(start = 8.dp)
                            )
                            OutlineText(
                                value = bio, onValueChange = { bio = it },
                                label = "Bio",
                                icons = Icons.Default.AddComment,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                )
                            )

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
                                icons = Icons.Default.Email,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                )
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

                            Spacer(modifier = Modifier.padding(top = 50.dp))
                            Button(
                                onClick = {
                                    if (name.isEmpty() || username.isEmpty() || bio.isEmpty() || email.isEmpty() || password.isEmpty() || imageUri == null) {
                                        Toast.makeText(
                                            context,
                                            "Please fill all fields",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        return@Button
                                    } else {
                                        viewModel.registerUser(
                                            email = email,
                                            password = password,
                                            name = name,
                                            userName = username,
                                            bio = bio,
                                            imageUri = imageUri!!,
                                            context = context
                                        )
                                    }


                                }, modifier = Modifier.fillMaxWidth(), colors = ButtonColors(
                                    contentColor = Color.White,
                                    containerColor = Blue40,
                                    disabledContentColor = Color.Gray,
                                    disabledContainerColor = Blue40
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "Sign up", fontSize = 16.sp,
                                    modifier = modifier.padding(10.dp)
                                )
                            }
                            Spacer(modifier = Modifier.padding(top = 30.dp))
                            if (state.value?.isLoading == true) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                            Spacer(modifier = Modifier.padding(top = 20.dp))

                            Row(
                                modifier = modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Already have an account?",
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Log in", fontWeight = FontWeight.ExtraBold,
                                    modifier = modifier
                                        .padding(start = 4.dp)
                                        .clickable {
                                            navController.navigate(AuthRouteScreen.Login.route)
                                        },
                                    color = Blue40
                                )
                            }
                        }
                        Spacer(modifier = Modifier.padding(top = 40.dp))

                    }

                    LaunchedEffect(key1 = state.value?.isSuccess) {
                        scope.launch {
                            if (state.value?.isSuccess?.isNotEmpty() == true) {
                                Toast.makeText(
                                    context,
                                    "You have successfully registered",
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
    label: String,
    keyboardOptions: KeyboardOptions
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
        keyboardOptions = keyboardOptions,
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
