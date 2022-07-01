package com.example.nikestore.feature.profile

import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.data.TokenContainer
import com.example.nikestore.data.repo.UserRepository

class ProfileViewModel(private val userRepository: UserRepository) : NikeViewModel() {
    val username: String
        get() = userRepository.getUserName()

    val isSignedIn: Boolean
        get() = TokenContainer.token != null

    fun signOut() = userRepository.signOut()
}