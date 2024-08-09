package com.example.friendzone.presentation.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.friendzone.nav.routes.AuthRouteScreen
import com.example.friendzone.nav.routes.Graph
import com.example.friendzone.viewmodel.auth.SignInViewModel
import kotlinx.coroutines.launch

@Composable
fun Login(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {

    val state = viewModel.signInState.collectAsState(initial = null)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

//    LaunchedEffect(firebaseUser) {
//        if (firebaseUser != null) {
//            navController.navigate(Routes.BottomNav.routes) {
//                popUpTo(Routes.Login.routes) {
//                    inclusive = true
//                }
//            }
//        }
//    }
//

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login Screen", fontSize = 30.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.padding(top = 30.dp))

        OutlineText(
            value = email, onValueChange = { email = it },
            label = "Email",
            icons = Icons.Default.Email
        )

        Spacer(modifier = Modifier.padding(top = 30.dp))

        OutlineText(
            value = password, onValueChange = { password = it },
            label = "Password",
            icons = Icons.Default.Lock
        )

        Spacer(modifier = Modifier.padding(top = 30.dp))

        Button(onClick = {
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@Button
            } else {
                viewModel.loginUser(email = email, password = password, context = context)
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Login")
        }

        if (state.value?.isLoading == true) {
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.padding(top = 30.dp))

        Button(onClick = {
            navController.navigate(AuthRouteScreen.Register.route)
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Register")
        }

    }
    LaunchedEffect(key1 = state.value?.isSuccess) {
        scope.launch {
            if (state.value?.isSuccess?.isNotEmpty() == true) {
                val success = state.value?.isSuccess
                Toast.makeText(context, "You have successfully login", Toast.LENGTH_SHORT)
                    .show()
                navController.navigate(Graph.MainScreenGraph){
                    popUpTo(Graph.AuthGraph){
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OutlineText(
    modifier: Modifier = Modifier,
    value: String,
    icons: ImageVector,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value.trim(),
        onValueChange = { onValueChange(it) },
        singleLine = true, leadingIcon = {
            Icon(
                imageVector = icons,
                contentDescription = "",
                modifier = Modifier.padding(10.dp), tint = Color.Gray
            )
        },
        label = {
            Text(
                text = "Type your $label", fontSize = 16.sp,
                fontWeight = FontWeight.W600, color = Color.Gray,
                fontFamily = FontFamily.SansSerif, maxLines = 1
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = Color.Gray,
            unfocusedTextColor = Color.Gray
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        modifier = modifier.fillMaxWidth(), minLines = 1
    )
}

