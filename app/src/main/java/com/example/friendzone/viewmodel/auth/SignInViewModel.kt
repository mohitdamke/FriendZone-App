package com.example.friendzone.viewmodel.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendzone.domain.auth.state.SignInState
import com.example.friendzone.presentation.repository.AuthRepository
import com.example.friendzone.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()



    fun loginUser(email: String, password: String, context : Context) = viewModelScope.launch {
        repository.loginUser(email, password, context).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _signInState.send(SignInState(isError = result.message!!))
                }

                is Resource.Loading -> {
                    _signInState.send(SignInState(isLoading = true))
                }

                is Resource.Success -> {
                    _signInState.send(SignInState(isSuccess = "Is Success Login"))
                }
            }
        }
    }
}