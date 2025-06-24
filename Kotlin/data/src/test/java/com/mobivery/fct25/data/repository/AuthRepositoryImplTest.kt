package com.mobivery.fct25.data.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.mobivery.fct25.data.datasources.APIDataSource
import com.mobivery.fct25.data.datasources.PreferencesDataSource
import com.mobivery.fct25.data.local.preferences.model.UserLocal
import com.mobivery.fct25.data.network.api.model.response.UserResponseData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

    @MockK
    private lateinit var mockAPIDataSource: APIDataSource

    @MockK
    private lateinit var mockPreferencesDataSource: PreferencesDataSource

    @MockK
    private lateinit var mockFirebaseAuth: FirebaseAuth

    @MockK
    private lateinit var mockAuthResult: AuthResult

    @MockK
    private lateinit var mockFirebaseUser: FirebaseUser

    private lateinit var authRepository: AuthRepositoryImpl

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { mockPreferencesDataSource.saveAccessToken(any()) } just runs
        every { mockPreferencesDataSource.saveRefreshToken(any()) } just runs
        coEvery { mockPreferencesDataSource.logout() } just runs
    }

    @Test
    fun givenValidFirebaseCredentials_whenLoginCalled_thenReturnsAccessTokenAndSavesTokens() =
        runTest {
            // GIVEN
            val user = "username"
            val pass = "password"
            val accessToken = "mock_access_token"

            val mockTokenResult = mockk<GetTokenResult> {
                every { token } returns accessToken
            }

            val mockAuthResultTask = Tasks.forResult(mockAuthResult)
            val mockTokenResultTask = Tasks.forResult(mockTokenResult)

            coEvery {
                mockFirebaseAuth.signInWithEmailAndPassword(
                    user,
                    pass
                )
            } returns mockAuthResultTask
            every { mockAuthResult.user } returns mockFirebaseUser
            coEvery { mockFirebaseUser.getIdToken(true) } returns mockTokenResultTask

            val mockUserResponse = mockk<UserResponseData>(relaxed = true)
            coEvery { mockAPIDataSource.getUserByUid() } returns mockUserResponse

            authRepository = AuthRepositoryImpl(
                mockAPIDataSource,
                mockPreferencesDataSource,
                mockFirebaseAuth
            )

            // WHEN
            every { mockFirebaseAuth.currentUser } returns mockFirebaseUser
            val result = authRepository.login(user, pass)

            // THEN
            assertEquals(accessToken, result.accessToken)
            verify { mockPreferencesDataSource.saveAccessToken(accessToken) }
            verify(exactly = 0) { mockPreferencesDataSource.saveRefreshToken(any()) }
        }

    @Test
    fun isUserLoggedTrueTest() = runTest {
        // GIVEN
        val mockUserLocal = UserLocal(
            email = "test@test.com",
            firebaseUid = "uid-test",
            role = 1,
            companyId = null
        )
        every { mockPreferencesDataSource.getUser() } returns flow {
            emit(mockUserLocal)
        }
        authRepository = AuthRepositoryImpl(
            mockAPIDataSource,
            mockPreferencesDataSource,
            mockFirebaseAuth
        )

        val collectJob = launch(UnconfinedTestDispatcher()) {
            authRepository.isUserLogged().collect()
        }

        // WHEN
        val userLoggedResult = authRepository.isUserLogged().first()

        // THEN
        assertTrue(userLoggedResult)
        collectJob.cancel()
    }

    @Test
    fun isUserLoggedFalseTest() = runTest {
        // GIVEN
        every { mockPreferencesDataSource.getUser() } returns flow {
            emit(null)
        }
        authRepository = AuthRepositoryImpl(
            mockAPIDataSource,
            mockPreferencesDataSource,
            mockFirebaseAuth
        )

        val collectJob = launch(UnconfinedTestDispatcher()) {
            authRepository.isUserLogged().collect()
        }

        // WHEN
        val userLoggedResult = authRepository.isUserLogged().first()

        // THEN
        assertFalse(userLoggedResult)
        collectJob.cancel()
    }

    @Test
    fun logoutTest() = runTest {
        // GIVEN
        authRepository = AuthRepositoryImpl(
            mockAPIDataSource,
            mockPreferencesDataSource,
            mockFirebaseAuth
        )

        // WHEN
        authRepository.logout()

        // THEN
        coVerify { mockPreferencesDataSource.logout() }
    }
}
