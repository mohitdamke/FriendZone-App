package com.example.friendzone.presentation.screens.post

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.dimension.FontDim
import com.example.friendzone.dimension.TextDim
import com.example.friendzone.ui.theme.DarkBlack
import com.example.friendzone.ui.theme.LightGray
import com.example.friendzone.ui.theme.White
import com.example.friendzone.ui.theme.brushAddPost
import com.example.friendzone.util.SharedPref
import com.example.friendzone.viewmodel.post.PostViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(modifier: Modifier = Modifier, navController: NavController) {
    val postViewModel: PostViewModel = viewModel()
    val isPosted by postViewModel.isPosted.observeAsState(false)

    val db = FirebaseDatabase.getInstance()
    val postRef = db.getReference("posts")

    val context = LocalContext.current

    var post by rememberSaveable { mutableStateOf("") }
    var imageUris by rememberSaveable { mutableStateOf<List<Uri>>(emptyList()) }

    LaunchedEffect(isPosted) {
        if (isPosted) {
            post = ""
            imageUris = emptyList()
            Toast.makeText(context, "Post has been successfully uploaded", Toast.LENGTH_SHORT)
                .show()
            navController.navigateUp()
        }
    }

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val imageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            imageUris = uris ?: emptyList()
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                imageLauncher.launch("image/*")
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
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
                        "CREATE POST", maxLines = 1,
                        letterSpacing = 1.sp, fontSize = TextDim.titleTextSize,
                        overflow = TextOverflow.Visible,
                        fontFamily = FontDim.Bold,
                    )
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (post.isEmpty()) {
                                Toast.makeText(context, "Enter Title", Toast.LENGTH_SHORT).show()
                                return@TextButton
                            }

                            val storeKey = postRef.push().key ?: return@TextButton

                            if (imageUris.isEmpty()) {
                                postViewModel.saveData(
                                    post = post,
                                    userId = FirebaseAuth.getInstance().currentUser!!.uid,
                                    storeKey = storeKey,
                                    images = emptyList()
                                )
                            } else {
                                postViewModel.saveImages(
                                    post = post,
                                    userId = FirebaseAuth.getInstance().currentUser!!.uid,
                                    imageUris = imageUris
                                )
                            }
                        },
                        modifier = modifier
                            .padding(top = 70.dp, bottom = 70.dp)
                            .clip(CircleShape)
                            .background(brushAddPost)
                    )
                    {
                        Text(
                            text = "POST",
                            fontSize = TextDim.titleTextSize,
                            fontFamily = FontDim.Bold,
                            color = White
                        )
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(DarkBlack)
                .padding(padding)
                .padding(10.dp)

        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* TODO: Handle profile image click */ }) {
                    Image(
                        painter = rememberAsyncImagePainter(model = SharedPref.getImageUrl(context)),
                        contentDescription = null,
                        modifier = modifier
                            .size(44.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = SharedPref.getName(context), fontSize = TextDim.titleTextSize,
                    fontFamily = FontDim.Bold,
                    color = White
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.padding(top = 10.dp))

            OutlinedTextField(
                value = post,
                onValueChange = { post = it },
                placeholder = {
                    Text(
                        text = "Type your Title",
                        fontSize = TextDim.secondaryTextSize,
                        fontFamily = FontDim.Medium,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Visible
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Go
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray,
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = White,
                    unfocusedTextColor = White
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(100.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Add Photo",
                        modifier = modifier
                            .size(30.dp)
                            .clickable {
                                val isGranted = ContextCompat.checkSelfPermission(
                                    context,
                                    permissionToRequest
                                ) == PackageManager.PERMISSION_GRANTED

                                if (isGranted) {
                                    imageLauncher.launch("image/*")
                                    Log.i("TAG", "Image is being loaded")
                                } else {
                                    permissionLauncher.launch(permissionToRequest)
                                    Log.i("TAG", "Image permission is not granted")
                                }
                            }, tint = LightGray

                    )
                }
            )

            Spacer(modifier = Modifier.padding(top = 10.dp))

            // Clear Button
            if (imageUris.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear Images",
                    tint = Color.White,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            imageUris = emptyList()
                            Log.i("TAG", "Cleared image URIs")
                        }
                        .padding(10.dp)
                )
            }

            // Image List
            LazyColumn(modifier = Modifier.padding(10.dp)) {
                items(imageUris) { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .size(150.dp)
                            .clip(RectangleShape)
                            .clickable {
                                imageUris = imageUris.filter { it != uri }
                            }
                    )
                }
            }
        }
    }
}
