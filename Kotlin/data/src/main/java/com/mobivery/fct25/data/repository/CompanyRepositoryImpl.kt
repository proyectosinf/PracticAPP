package com.mobivery.fct25.data.repository

import android.net.Uri
import com.mobivery.fct25.data.datasources.APIDataSource
import com.mobivery.fct25.data.datasources.ImageUriDataSource
import com.mobivery.fct25.data.error.errorWrapper
import com.mobivery.fct25.data.network.api.UploadImageApi
import com.mobivery.fct25.data.network.api.model.request.CreateCompanyRequestData.Companion.toRequestData
import com.mobivery.fct25.data.network.api.model.response.toDomain
import com.mobivery.fct25.domain.repository.AuthRepository
import com.mobivery.template.domain.model.company.CompanyModel
import com.mobivery.template.domain.repository.CompanyRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class CompanyRepositoryImpl @Inject constructor(
    private val apiDataSource: APIDataSource,
    private val authRepository: AuthRepository,
    private val uploadImageApi: UploadImageApi,
    private val imageUriDataSource: ImageUriDataSource
) : CompanyRepository {

    companion object {
        private const val IMAGE_MIME_TYPE = "image/jpeg"
        private const val UPLOAD_MODEL_COMPANY = 3
        private const val FORM_FILE_PART_NAME = "file"
        private const val LOGO_NAME = "logo.jpg"
        private const val ERROR_CANNOT_OPEN_IMAGE_URI = "Unable to open image URI"
    }

    override suspend fun createCompany(company: CompanyModel) {
        errorWrapper {
            authRepository.refreshTokenIfNeeded()
            val request = company.toRequestData()
            apiDataSource.createCompany(request)
        }
    }

    override suspend fun getCurrentUserCompany(): CompanyModel {
        return errorWrapper {
            authRepository.refreshTokenIfNeeded()
            apiDataSource.getCurrentUserCompany().toDomain()
        }
    }

    override suspend fun uploadCompanyImage(uri: Uri) {
        errorWrapper {
            authRepository.refreshTokenIfNeeded()

            imageUriDataSource.openInputStream(uri)?.use { inputStream ->
                val bytes = inputStream.readBytes()
                val requestFile = bytes.toRequestBody(IMAGE_MIME_TYPE.toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData(
                    FORM_FILE_PART_NAME,
                    LOGO_NAME,
                    requestFile
                )
                uploadImageApi.uploadImage(model = UPLOAD_MODEL_COMPANY, file = body)
            } ?: throw IllegalArgumentException(ERROR_CANNOT_OPEN_IMAGE_URI)
        }
    }
}
