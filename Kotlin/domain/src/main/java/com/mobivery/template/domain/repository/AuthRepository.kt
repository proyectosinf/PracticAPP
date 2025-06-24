package com.mobivery.fct25.domain.repository

import com.mobivery.fct25.domain.model.auth.UserCredentials
import com.mobivery.template.domain.model.auth.RegisterResult
import com.mobivery.template.domain.model.auth.UserSession
import com.mobivery.template.domain.model.user.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(user: String, pass: String): UserCredentials
    fun isUserLogged(): Flow<Boolean>
    suspend fun logout()
    suspend fun register(
        user: User,
        password: String,
        firebaseToken: String
    ): RegisterResult
    suspend fun saveAccessToken(token: String)
    suspend fun saveFirebaseToken(token: String)
    fun getUserLocal(): Flow<UserSession?>
    suspend fun refreshTokenIfNeeded()
    suspend fun sendPasswordResetEmail(email: String)
}
