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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.R
import com.example.friendzone.dimension.FontDim
import com.example.friendzone.dimension.TextDim
import com.example.friendzone.nav.routes.AuthRouteScreen
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.ui.theme.DarkBlack
import com.example.friendzone.ui.theme.SocialBlue
import com.example.friendzone.ui.theme.SocialPink
import com.example.friendzone.ui.theme.brushAddPost
import com.example.friendzone.viewmodel.auth.SignUpViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
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
            .background(DarkBlack)
    ) {
        LazyColumn {
            item {
                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.padding(top = 60.dp))
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
                    Spacer(modifier = Modifier.padding(top = 70.dp))

                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Create an account",
                            fontSize = 26.sp,
                            fontFamily = FontDim.Bold,
                            color = White, textAlign = TextAlign.Center
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
                        Spacer(modifier = modifier.padding(top = 10.dp))

                        if (imageUri == null) {
                            Text(
                                text = "Upload your profile picture",
                                fontSize = TextDim.secondaryTextSize,
                                fontFamily = FontDim.Bold,
                                color = com.example.friendzone.ui.theme.White
                            )
                        } else {
                            Text(
                                text = "Change your profile picture",
                                fontSize = TextDim.secondaryTextSize,
                                fontFamily = FontDim.Bold,
                                color = com.example.friendzone.ui.theme.White
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                    ) {

                        Spacer(modifier = Modifier.padding(top = 16.dp))

                        Text(
                            text = "Full name",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = White,
                            fontFamily = FontDim.Normal,
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

                        Spacer(modifier = Modifier.padding(top = 16.dp))

                        Text(
                            text = "Username",
                            fontSize = 16.sp,
                            color = White,
                            fontFamily = FontDim.Normal,
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

                        Spacer(modifier = Modifier.padding(top = 16.dp))

                        Text(
                            text = "Bio",
                            fontSize = 16.sp,
                            color = White,
                            fontFamily = FontDim.Normal,
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

                        Spacer(modifier = Modifier.padding(top = 16.dp))

                        Text(
                            text = "Email",
                            fontSize = 16.sp, color = White,
                            fontFamily = FontDim.Normal,
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

                        Spacer(modifier = Modifier.padding(top = 16.dp))

                        Text(
                            text = "Password",
                            fontSize = 16.sp, color = White,
                            fontFamily = FontDim.Normal,
                            fontWeight = FontWeight.Medium,
                            modifier = modifier.padding(start = 8.dp)
                        )
                        PassOutlineText(
                            value = password, onValueChange = { password = it },
                            label = "Password",
                            icons = Icons.Default.Lock
                        )

                        Spacer(modifier = Modifier.padding(top = 50.dp))
                        TextButton(
                            onClick = {
                                if (name.isEmpty() || username.isEmpty() || bio.isEmpty() || email.isEmpty() || password.isEmpty() || imageUri == null) {
                                    Toast.makeText(
                                        context,
                                        "Please fill all fields",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    return@TextButton
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


                            }, modifier = Modifier
                                .fillMaxWidth()
                                .clip(CircleShape)
                                .background(brushAddPost),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Sign up",
                                fontSize = TextDim.titleTextSize,
                                fontFamily = FontDim.Bold,
                                color = com.example.friendzone.ui.theme.White
                            )
                        }
                        Spacer(modifier = Modifier.padding(top = 16.dp))
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
                                fontWeight = FontWeight.Medium, color = White,
                                fontFamily = FontDim.Normal
                            )
                            Text(
                                text = "Log in",
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontDim.Normal,
                                modifier = modifier
                                    .padding(start = 4.dp)
                                    .clickable {
                                        navController.navigate(AuthRouteScreen.Login.route)
                                    },
                                color = SocialPink
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
                modifier = Modifier.padding(10.dp), tint = White
            )
        },
        label = {
            Text(
                text = "Enter your $label", fontSize = TextDim.secondaryTextSize,
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
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            focusedContainerColor = DarkBlack,
            unfocusedContainerColor = DarkBlack,
            focusedTextColor = com.example.friendzone.ui.theme.White,
            unfocusedTextColor = com.example.friendzone.ui.theme.White
        ),
        keyboardOptions = keyboardOptions,
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
        label = {
            Text(
                text = "Enter your $label", fontSize = TextDim.secondaryTextSize,
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
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            focusedContainerColor = DarkBlack,
            unfocusedContainerColor = DarkBlack,
            focusedTextColor = com.example.friendzone.ui.theme.White,
            unfocusedTextColor = com.example.friendzone.ui.theme.White
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        visualTransformation = PasswordVisualTransformation(),

        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(100.dp),
        minLines = 1
    )
}
