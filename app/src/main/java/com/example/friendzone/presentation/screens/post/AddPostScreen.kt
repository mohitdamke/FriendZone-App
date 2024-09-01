package com.example.friendzone.presentation.screens.post

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.ui.theme.Blue40
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
    val scope = rememberCoroutineScope()

    var post by rememberSaveable { mutableStateOf("") }
    var imageUris by rememberSaveable { mutableStateOf<List<Uri>>(emptyList()) }

    LaunchedEffect(isPosted) {
        if (isPosted) {
            post = ""
            imageUris = emptyList()
            Toast.makeText(context, "Post has been successfully uploaded", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
        }
    }

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        imageUris = uris ?: emptyList()
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            imageLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        "Make a Post",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Blue40,
                        modifier = modifier.padding(start = 100.dp)
                    )
                },
                actions = {
                    IconButton(onClick = {
                        if (post.isEmpty()) {
                            Toast.makeText(context, "Enter Title", Toast.LENGTH_SHORT).show()
                            return@IconButton
                        }

                        val storeKey = postRef.push().key ?: return@IconButton

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
                    }) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Blue40
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
                .padding(padding)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
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
                    text = SharedPref.getUserName(context),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = post,
                onValueChange = { post = it },
                label = { Text("Enter Title") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
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
                            }
                    )
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Clear Button
            if (imageUris.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear Images",
                    tint = Color.Black,
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
                                Log.i("TAG", "Image removed: $uri")
                            }
                    )
                }
            }
        }
    }
}
