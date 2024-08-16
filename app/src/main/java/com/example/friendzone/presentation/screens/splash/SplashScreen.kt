package com.example.friendzone.presentation.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.friendzone.R
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.ui.theme.Blue40
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

        Image(
            painter = painterResource(id = R.drawable.fz_1),
            contentDescription = null,
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
        )

    }
//        Text(
//            text = "FZ",
//            fontSize = 100.sp,
//            fontWeight = FontWeight.Bold,
//            fontStyle = FontStyle.Italic ,
//            color = Blue40
//        )
//        Text(text = "FriendZone",
//            fontSize = 26.sp,
//            fontWeight = FontWeight.Normal,
//            fontStyle = FontStyle.Normal ,
//            color = Blue40, letterSpacing = 10.sp)
//    }
    LaunchedEffect(key1 = true) {
        delay(1000)

        if (FirebaseAuth.getInstance().currentUser == null) {
            navController.navigate(Graph.AuthGraph) {
                popUpTo(Graph.SplashGraph) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(Graph.MainScreenGraph) {
                popUpTo(Graph.SplashGraph) {
                    inclusive = true
                }
            }
        }
    }
}
