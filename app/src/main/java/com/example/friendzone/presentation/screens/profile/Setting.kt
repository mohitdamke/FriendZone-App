package com.example.friendzone.presentation.screens.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.friendzone.dimension.FontDim
import com.example.friendzone.dimension.TextDim
import com.example.friendzone.nav.routes.AuthRouteScreen
import com.example.friendzone.nav.routes.MainRouteScreen
import com.example.friendzone.nav.routes.ProfileRouteScreen
import com.example.friendzone.ui.theme.DarkBlack
import com.example.friendzone.ui.theme.White
import com.example.friendzone.viewmodel.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Setting(modifier: Modifier = Modifier, navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    var currentUserId = ""
    if (FirebaseAuth.getInstance().currentUser != null) currentUserId =
        FirebaseAuth.getInstance().currentUser!!.uid

    LaunchedEffect(key1 = currentUserId) {
        if (currentUserId.isEmpty()) {
            navController.navigate(AuthRouteScreen.Login.route) {
                popUpTo(ProfileRouteScreen.Settings.route) {
                    inclusive = true
                }
            }
        } else {
            Unit
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
                    text = "SETTING",
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
    ) { padding ->


        Column(
            modifier = modifier
                .fillMaxSize()
                .background(DarkBlack)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "You want to logout", fontSize = TextDim.titleTextSize,
                fontFamily = FontDim.Normal,
                color = White
            )
            Spacer(modifier = Modifier.padding(top = 10.dp))
            Button(
                onClick = {
                    scope.launch {
                        navController.navigate(
                            AuthRouteScreen.Login.route
                        ) {
                            popUpTo(MainRouteScreen.Profile.route) {
                                inclusive = true
                            }
                        }
                        authViewModel.logout()
                        Toast.makeText(
                            context,
                            "You have successfully Logout",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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
                    text = "Logout", fontSize = TextDim.bodyTextSize,
                    fontFamily = FontDim.Normal,
                )
            }
        }
    }
}