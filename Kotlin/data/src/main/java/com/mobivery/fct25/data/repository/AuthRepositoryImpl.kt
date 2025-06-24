package com.mobivery.fct25.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.mobivery.fct25.data.datasources.APIDataSource
import com.mobivery.fct25.data.datasources.PreferencesDataSource
import com.mobivery.fct25.data.local.preferences.model.UserLocal
import com.mobivery.fct25.data.network.api.model.request.RegisterRequestData
import com.mobivery.fct25.data.network.api.model.response.toDomain
import com.mobivery.fct25.data.network.constants.DEFAULT_USER_PHOTO_URL
import com.mobivery.fct25.data.network.constants.ERROR_TOKEN_NULL
import com.mobivery.fct25.domain.model.auth.UserCredentials
import com.mobivery.fct25.domain.model.error.AppError
import com.mobivery.fct25.domain.repository.AuthRepository
import com.mobivery.template.domain.model.auth.RegisterResult
import com.mobivery.template.domain.model.auth.UserSession
import com.mobivery.template.domain.model.user.User
import com.mobivery.template.domain.model.user.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiDataSource: APIDataSource,
    private val preferencesDataSource: PreferencesDataSource,
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

    override suspend fun login(user: String, password: String): UserCredentials {
        val result = firebaseAuth.signInWithEmailAndPassword(user, password).await()

        val token = result.user?.getIdToken(true)?.await()
            ?: throw Exception(ERROR_TOKEN_NULL)

        val accessToken = token.token ?: throw Exception(ERROR_TOKEN_NULL)
        preferencesDataSource.saveAccessToken(accessToken)

        refreshTokenIfNeeded()

        val userResponse = apiDataSource.getUserByUid()
        preferencesDataSource.saveUser(
            UserLocal(
                email = userResponse.email,
                firebaseUid = userResponse.uid,
                role = userResponse.role,
                companyId = userResponse.companyId
            )
        )

        return UserCredentials(accessToken, null)
    }

    override fun isUserLogged(): Flow<Boolean> =
        preferencesDataSource.getUser().map { it != null }.catch { emit(false) }

    override suspend fun logout() {
        firebaseAuth.signOut()
        preferencesDataSource.logout()
    }

    override suspend fun register(
        user: User,
        password: String,
        firebaseToken: String
    ): RegisterResult {
        val finalPhoto = user.photo?.takeIf { it.isNotBlank() } ?: DEFAULT_USER_PHOTO_URL

        val request = RegisterRequestData(
            email = user.email,
            name = user.name,
            surname = user.surname,
            role = when (user.role) {
                UserRole.STUDENT -> 1
                UserRole.TUTOR -> 2
            },
            securityCode = null,
            dni = user.dni?.takeIf { it.isNotBlank() },
            socialSecurityNumber = user.socialSecurityNumber?.takeIf { it.isNotBlank() },
            contactName = user.contactName?.takeIf { it.isNotBlank() },
            contactPhone = user.contactPhone?.takeIf { it.isNotBlank() },
            contactEmail = user.contactEmail?.takeIf { it.isNotBlank() },
            firebaseUid = user.firebaseUid,
            photo = finalPhoto,
            pdfCv = user.pdfCV?.takeIf { it.isNotBlank() },
            companyId = user.companyId
        )

        preferencesDataSource.saveAccessToken(firebaseToken)

        refreshTokenIfNeeded()

        val response = when (user.role) {
            UserRole.STUDENT -> apiDataSource.registerStudent(request)
            UserRole.TUTOR -> apiDataSource.registerWorkTutor(request)
        }

        response.accessToken?.let { preferencesDataSource.saveAccessToken(it) }
        response.refreshToken?.let { preferencesDataSource.saveRefreshToken(it) }

        preferencesDataSource.saveUser(
            UserLocal(
                email = user.email,
                firebaseUid = user.firebaseUid,
                role = when (user.role) {
                    UserRole.STUDENT -> 1
                    UserRole.TUTOR -> 2
                },
                companyId = user.companyId
            )
        )

        return response.toDomain()
    }

    override suspend fun saveAccessToken(token: String) {
        preferencesDataSource.saveAccessToken(token)
    }

    override suspend fun saveFirebaseToken(token: String) {
        preferencesDataSource.saveFirebaseToken(token)
    }

    override fun getUserLocal(): Flow<UserSession?> {
        return preferencesDataSource.getUser().map { local ->
            local?.let {
                UserSession(
                    email = it.email,
                    firebaseUid = it.firebaseUid,
                    role = it.role,
                    companyId = it.companyId
                )
            }
        }
    }

    override suspend fun refreshTokenIfNeeded() {
        try {
            val currentUser = firebaseAuth.currentUser ?: throw AppError.UnauthorizedError()
            val tokenResult = currentUser.getIdToken(true).await()
            val accessToken = tokenResult.token ?: throw AppError.UnauthorizedError()
            preferencesDataSource.saveAccessToken(accessToken)
        } catch (e: Exception) {
            throw AppError.UnauthorizedError()
        }
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }
}
