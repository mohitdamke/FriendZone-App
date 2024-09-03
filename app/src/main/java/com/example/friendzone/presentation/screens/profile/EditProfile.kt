package com.example.friendzone.presentation.screens.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.R
import com.example.friendzone.dimension.FontDim
import com.example.friendzone.dimension.TextDim
import com.example.friendzone.ui.theme.DarkBlack
import com.example.friendzone.ui.theme.White
import com.example.friendzone.viewmodel.auth.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(),
    navController: NavController,
) {
    var username by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current


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


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = DarkBlack,
                titleContentColor = White,
                actionIconContentColor = White,
                navigationIconContentColor = White,
                scrolledContainerColor = DarkBlack,
            ), title = {
                Text(
                    text = "EDIT PROFILE",
                    maxLines = 1,
                    letterSpacing = 1.sp, fontSize = TextDim.titleTextSize,
                    overflow = TextOverflow.Visible,
                    fontFamily = FontDim.Bold,
                )
            }, navigationIcon = {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = modifier
                        .size(30.dp)
                        .clickable {
                            navController.navigateUp()

                        })

            }

            )
        },
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(DarkBlack)
                .padding(paddingValues)
                .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.padding(top = 16.dp))
            Image(
                painter = if (imageUri == null) {
                    painterResource(id = R.drawable.man)
                } else {
                    rememberAsyncImagePainter(
                        model = imageUri,
                    )
                },
                contentDescription = null,
                modifier = modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable {
                        val isGranted = ContextCompat.checkSelfPermission(
                            context, permissionToRequest
                        ) == PackageManager.PERMISSION_GRANTED

                        if (isGranted) {
                            launcher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permissionToRequest)
                        }

                    },
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = modifier.padding(top = 10.dp))
            if (imageUri == null) {

                Text(
                    text = "Upload your profile picture",
                    fontSize = TextDim.secondaryTextSize,
                    fontFamily = FontDim.Bold,
                    color = White
                )
            } else {
                Text(
                    text = "Change your profile picture",
                    fontSize = TextDim.secondaryTextSize,
                    fontFamily = FontDim.Bold,
                    color = White
                )

            }
            Spacer(modifier = Modifier.height(30.dp))

            EditProfileOutlineText(
                value = username,
                onValueChange = { username = it },
                label = "Username",
                icons = Icons.Default.Person2,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            EditProfileOutlineText(
                value = name,
                onValueChange = { name = it },
                label = "Name",
                icons = Icons.Default.Person,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            EditProfileOutlineText(
                value = bio,
                onValueChange = { bio = it },
                label = "Bio",
                icons = Icons.Default.AddComment,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Button(
                onClick = {
                    authViewModel.updateProfile(
                        name = name.ifEmpty { null },
                        bio = bio.ifEmpty { null },
                        userName = username.ifEmpty { null },
                        imageUri = imageUri,
                        context = context
                    )
                    navController.navigateUp()
                },
                elevation = ButtonDefaults.buttonElevation(10.dp),
                border = ButtonDefaults.outlinedButtonBorder,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(40.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkBlack, contentColor = Color.White
                )

            ) {
                Text(
                    "Update Profile", fontSize = TextDim.bodyTextSize,
                    fontFamily = FontDim.Normal,
                )

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileOutlineText(
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
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = icons,
                contentDescription = "",
                modifier = Modifier.padding(10.dp),
                tint = White
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
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedPlaceholderColor = Color.Gray,
            focusedPlaceholderColor = Color.Gray,
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = White,
            unfocusedTextColor = White
        ),
        keyboardOptions = keyboardOptions,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(100.dp),
        minLines = 1
    )
}

