package com.example.friendzone.domain.auth.state

data class PostState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val isError: String? = null
)