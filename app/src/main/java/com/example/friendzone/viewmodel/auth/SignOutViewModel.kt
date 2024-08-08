package com.example.friendzone.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendzone.domain.auth.state.SignOutState
import com.example.friendzone.presentation.repository.AuthRepository
import com.example.friendzone.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignOutViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _signOutState = Channel<SignOutState>()
    val signOutState = _signOutState.receiveAsFlow()


    fun signOutUser() = viewModelScope.launch {
        repository.signOutUser().collect { result ->
            when (result) {
                is Resource.Error -> {
                    _signOutState.send(SignOutState(isError = result.message!!))
                }

                is Resource.Loading -> {
                    _signOutState.send(SignOutState(isLoading = true))
                }

                is Resource.Success -> {
                    _signOutState.send(SignOutState(isSuccess = "Is Success SignOut"))
                }
            }
        }
    }
}