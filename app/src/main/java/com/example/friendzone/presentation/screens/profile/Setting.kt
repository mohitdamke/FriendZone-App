package com.example.friendzone.presentation.screens.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.friendzone.nav.routes.AuthRouteScreen
import com.example.friendzone.nav.routes.MainRouteScreen
import com.example.friendzone.nav.routes.ProfileRouteScreen
import com.example.friendzone.ui.theme.Blue40
import com.example.friendzone.viewmodel.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

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


    Scaffold { padding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Setting",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Blue40
                )
            }
            Spacer(modifier = modifier.padding(top = 200.dp))
            Text(text = "You want to logout", fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
                }, modifier = Modifier
                    .fillMaxWidth(), colors = ButtonColors(
                    contentColor = Color.White,
                    containerColor = Blue40,
                    disabledContentColor = Color.Gray,
                    disabledContainerColor = Blue40
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Logout", fontSize = 16.sp,
                    modifier = modifier.padding(10.dp)
                )
            }
        }
    }
}