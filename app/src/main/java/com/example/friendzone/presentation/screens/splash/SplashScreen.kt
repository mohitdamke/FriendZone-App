package com.example.friendzone.presentation.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.friendzone.R
import com.example.friendzone.nav.routes.Graph
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.thread), contentDescription = null)

    }
    LaunchedEffect(key1 = true) {
        delay(1000)

        if (FirebaseAuth.getInstance().currentUser == null) {
            navController.navigate(Graph.AuthGraph){
                popUpTo(Graph.SplashGraph){
                    inclusive = true
                }
            }
        } else {
            navController.navigate(Graph.MainScreenGraph){
                popUpTo(Graph.SplashGraph){
                    inclusive = true
                }
            }
        }
    }
}
