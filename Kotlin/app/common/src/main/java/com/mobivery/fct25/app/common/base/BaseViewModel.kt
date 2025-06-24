package com.mobivery.fct25.app.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobivery.fct25.domain.model.error.AppError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

interface BaseViewModelInterface {
    val loading: StateFlow<Boolean>
    val error: StateFlow<AppError?>
    fun closeError()
}

open class BaseViewModel : ViewModel(), BaseViewModelInterface {

    private val loadingCount = AtomicInteger(0)
    override val loading = MutableStateFlow(false)
    override val error = MutableStateFlow<AppError?>(null)
    private var hasAlreadyHandledSessionExpiration = false

    /**
     * Executes a 'suspend' function, managing loading and errors. Must be used inside a
     * 'viewModelScope.launch', just like it were if this method weren't used
     * @param showLoading If a loading dialog is displayed while executing the job.
     * @param showDefaultError If a error dialog is displayed when job ends with error.
     * @param onError Function that is executed after job ends unsuccessfully. Error message
     * @param job function with the action to be performed
     */
    protected suspend fun errorWrapper(
        showLoading: Boolean = true,
        showDefaultError: Boolean = true,
        onError: suspend (AppError) -> Unit = {},
        job: suspend () -> Unit,
    ) {
        if (showLoading) {
            showLoading()
        }
        try {
            job()
        } catch (throwable: Throwable) {
            if (throwable is AppError) {
                if (showDefaultError) {
                    this.error.value = throwable
                }
                onError(throwable)
            } else {
                val error = AppError.Unknown(throwable.message)
                if (showDefaultError) {
                    this.error.value = error
                }
                onError(error)
            }
        } finally {
            if (showLoading) {
                val loadingCount = loadingCount.decrementAndGet()
                if (loadingCount < 0) {
                    this.loadingCount.set(0)
                }
                if (loadingCount <= 0) {
                    loading.value = false
                }
            }
        }
    }

    override fun closeError() {
        viewModelScope.launch {
            error.value = null
        }
    }

    protected fun showLoading() {
        loadingCount.incrementAndGet()
        loading.value = true
    }

    protected fun launchWithErrorWrapper(
        showLoading: Boolean = true,
        showDefaultError: Boolean = true,
        onError: suspend (AppError) -> Unit = {},
        job: suspend () -> Unit,
    ) = viewModelScope.launch {
        errorWrapper(
            showLoading = showLoading,
            showDefaultError = showDefaultError,
            onError = { throwable ->
                if (throwable is AppError.UnauthorizedError) {
                    if (!hasAlreadyHandledSessionExpiration) {
                        hasAlreadyHandledSessionExpiration = true
                        onError(throwable)
                    }
                } else {
                    onError(throwable)
                }
            },
            job = job
        )
    }
}
