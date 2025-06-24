package com.mobivery.fct25.data.extension

import java.util.Locale

fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}