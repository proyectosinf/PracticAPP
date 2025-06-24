package com.mobivery.fct25.data.datasources

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

class ImageUriDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun openInputStream(uri: Uri): InputStream? {
        return context.contentResolver.openInputStream(uri)
    }
}
