package com.mobivery.fct25.app.common.extension

import java.time.LocalDate
import java.time.ZoneId

fun LocalDate.toMillis() = this.atStartOfDay(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()