package com.example.friendzone.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.friendzone.navigation.Routes
import com.example.friendzone.presentation.top.TopBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()
) {
    Scaffold(
        topBar = { TopBar() },
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Home Screen")
            Button(onClick = { navController.navigate(Routes.DetailScreen.routes) }) {
                Text(text = "Detail")
            }

        }
    }
}
