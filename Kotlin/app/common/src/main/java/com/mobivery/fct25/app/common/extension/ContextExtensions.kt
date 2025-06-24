package com.mobivery.fct25.app.common.extension

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import android.provider.Settings
import android.webkit.MimeTypeMap
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.FileProvider
import java.io.File

private val imageFileExtensions = listOf(
    "jpg",
    "png",
    "jpeg"
)
private const val PDF_EXTENSION = "pdf"
private const val TEMP_IMAGE_DIRECTORY = "images"
private const val TEMP_IMAGE_PREFIX = "camera_image_"
private const val TEMP_IMAGE_SUFFIX = ".jpg"

fun Context.launchCustomTab(url: String, toolbarColor: Color) {
    if (url.isNotEmpty()) {
        val colorInt: Int = toolbarColor.toArgb()

        val defaultColors = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(colorInt)
            .build()
        val customTabsIntent =
            CustomTabsIntent.Builder().setDefaultColorSchemeParams(defaultColors).setShowTitle(true)
                .build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }
}

fun Context.openMapWithCoordinates(latitude: Double, longitude: Double) {
    val uri = "geo:0,0?q=$latitude,$longitude"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    startActivity(intent)
}

fun Context.addEventToCalendar(
    title: String,
    startMillis: Long,
    endMillis: Long
) {
    val intent = Intent(Intent.ACTION_INSERT)
        .setData(CalendarContract.Events.CONTENT_URI)
        .putExtra(CalendarContract.Events.TITLE, title)
        .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)

    startActivity(intent)
}

fun Context.openFile(file: File, openImage: (String) -> Unit, openFileError: () -> Unit) {
    val extension = file.extension.lowercase()
    when {
        imageFileExtensions.contains(extension) -> {
            openImage(file.absolutePath)
        }
        else -> {
            val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            val fileUri =
                FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", file)

            val intent = Intent().apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                action = Intent.ACTION_VIEW
                setDataAndType(fileUri, type)
            }
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // No activity can handle the intent.
                openFileError()
            }
        }
    }
}

fun Context.getTempImageUri(): Uri {
    val directory = File(this.cacheDir, TEMP_IMAGE_DIRECTORY)
    directory.mkdirs()
    val file = File.createTempFile(
        TEMP_IMAGE_PREFIX,
        TEMP_IMAGE_SUFFIX,
        directory,
    )
    val authority = this.packageName + ".provider"

    return FileProvider.getUriForFile(
        this,
        authority,
        file,
    )
}

fun Context.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", this.packageName, null)
    )
    this.startActivity(intent)
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}