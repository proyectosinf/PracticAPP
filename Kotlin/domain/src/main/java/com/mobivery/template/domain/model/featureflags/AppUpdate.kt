package com.mobivery.fct25.domain.model.featureflags

data class AppUpdate(
    val isActive: Boolean?,
    val force: Boolean?,
    val fromVersion: AppVersion?,
    val toVersion: AppVersion?,
)
