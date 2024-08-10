package com.example.friendzone.viewmodel.auth

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendzone.domain.auth.state.auth.SignUpState
import com.example.friendzone.presentation.repository.auth.AuthRepository
import com.example.friendzone.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _signUpState = Channel<SignUpState>()
    val signUpState = _signUpState.receiveAsFlow()

    fun registerUser(
        email: String, password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri,
        context: Context,
    ) = viewModelScope.launch {
        repository.registerUser(
            email = email,
            password = password,
            name = name,
            bio = bio,
            userName = userName,
            imageUri = imageUri,
            context = context,
        ).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _signUpState.send(SignUpState(isError = result.message!!))
                }

                is Resource.Loading -> {
                    _signUpState.send(SignUpState(isLoading = true))
                }

                is Resource.Success -> {
                    _signUpState.send(SignUpState(isSuccess = "Is Success Register"))
                }
            }
        }
    }
}