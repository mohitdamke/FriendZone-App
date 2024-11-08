package com.example.friendzone.presentation.screens.story

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.friendzone.ui.theme.White
import com.example.friendzone.ui.theme.brushAddPost
import com.example.friendzone.util.SharedPref
import com.example.friendzone.viewmodel.story.AddStoryViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStory(modifier: Modifier = Modifier, navController: NavController) {
    val addStoryViewModel: AddStoryViewModel = viewModel()
    val isPosted by addStoryViewModel.isPosted.observeAsState(false)

    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }


    LaunchedEffect(isPosted) {
        if (isPosted) {
            imageUri = null

            navController.navigateUp()

            Toast.makeText(
                context,
                "story have been Successfully Uploaded",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        )
        { uri: Uri? ->
            imageUri = uri
        }
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
            } else {
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkBlack,
                    titleContentColor = White,
                    actionIconContentColor = White,
                    navigationIconContentColor = White,
                    scrolledContainerColor = DarkBlack,
                ),
                title = {
                    Text(
                        "STORY", maxLines = 1,
                        letterSpacing = 1.sp, fontSize = TextDim.titleTextSize,
                        overflow = TextOverflow.Visible,
                        fontFamily = FontDim.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = modifier.size(28.dp)

                        )
                    }
                },
                actions = {

                    TextButton(
                        onClick = {
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            if (currentUser != null) {
                                if (imageUri == null) {
                                    addStoryViewModel.saveStory(
                                        userId = FirebaseAuth.getInstance().currentUser!!.uid,
                                        storyKey = "",  // Provide a default key or handle it separately
                                        imageUrl = ""
                                    )
                                } else {
                                    addStoryViewModel.saveImage(
                                        userId = FirebaseAuth.getInstance().currentUser!!.uid,
                                        imageUri = imageUri!!
                                    )
                                }
                            }
                        },
                        modifier = modifier
                            .padding(top = 70.dp, bottom = 70.dp)
                            .clip(CircleShape)
                            .background(brushAddPost)

                    ) {
                        Text(
                            text = "PUBLISH",
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
                .padding(padding),
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                IconButton(onClick = { /* TODO something */ }) {
                    Image(
                        painter = rememberAsyncImagePainter(model = SharedPref.getImageUrl(context)),
                        contentDescription = null, modifier = Modifier
                            .clip(CircleShape)
                            .size(44.dp), contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.padding(start = 10.dp))
                Text(
                    text = SharedPref.getUserName(context),
                    fontSize = TextDim.titleTextSize,
                    fontFamily = FontDim.Bold,
                    color = White
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp), contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUri),
                        contentDescription = "Selected Image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RectangleShape)
                    )
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Close Icon",
                        tint = Color.White,
                        modifier = Modifier
                            .size(60.dp)
                            .clickable {
                                imageUri = null
                            }
                            .padding(top = 14.dp, bottom = 10.dp)
                            .align(Alignment.TopEnd)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Photo",
                        tint = White,
                        modifier = Modifier
                            .size(100.dp)
                            .clickable {
                                val isGranted = ContextCompat.checkSelfPermission(
                                    context,
                                    permissionToRequest
                                ) == PackageManager.PERMISSION_GRANTED

                                if (isGranted) {
                                    launcher.launch("image/*")
                                    Log.i("TAG", "Image is being loaded")
                                } else {
                                    permissionLauncher.launch(permissionToRequest)
                                    Log.i("TAG", "Image permission is not granted")
                                }
                            }
                    )
                }

            }
        }
    }
}
