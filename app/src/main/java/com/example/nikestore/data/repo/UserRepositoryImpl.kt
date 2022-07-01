package com.example.nikestore.data.repo

import com.example.nikestore.data.TokenContainer
import com.example.nikestore.data.TokenResponse
import com.example.nikestore.data.repo.source.UserDataSource
import io.reactivex.Completable

class UserRepositoryImpl(
    private val userLocalDataSource: UserDataSource,
    private val userRemoteDataSource: UserDataSource
) : UserRepository {
    override fun login(username: String, password: String): Completable {
        return userRemoteDataSource.login(username, password).doOnSuccess {
            onSuccessfulLogin(username, it)
        }.ignoreElement()
    }

    override fun signUp(username: String, password: String): Completable {
        return userRemoteDataSource.signUp(username, password).flatMap {
            userRemoteDataSource.login(username, password)
        }.doOnSuccess {
            onSuccessfulLogin(username, it)
        }.ignoreElement()
    }

    override fun loadToken() {
        userLocalDataSource.loadToken()
    }

    override fun getUserName(): String = userLocalDataSource.getUsername()
    override fun signOut() {
        userLocalDataSource.signOut()
        TokenContainer.update(null, null)
    }

    fun onSuccessfulLogin(username: String, tokenResponse: TokenResponse) {
        TokenContainer.update(tokenResponse.access_token, tokenResponse.refresh_token)
        userLocalDataSource.saveToken(tokenResponse.access_token, tokenResponse.refresh_token)
        userLocalDataSource.saveUsername(username)
    }
}