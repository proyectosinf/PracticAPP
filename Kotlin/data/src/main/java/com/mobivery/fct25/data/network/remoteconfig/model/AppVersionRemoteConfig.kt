package com.mobivery.fct25.data.model

import com.mobivery.fct25.domain.model.featureflags.AppVersion

data class AppVersionRemoteConfig(val major: Int, val minor: Int, val patch: Int) : Comparable<AppVersionRemoteConfig> {

    constructor(version: String) : this(
        version.substringBefore('.').toInt(),
        version.substringAfter('.').substringBefore('.').toInt(),
        version.substringAfterLast('.').toInt()
    )

    override fun toString(): String {
        return "$major.$minor.$patch"
    }

    override fun compareTo(other: AppVersionRemoteConfig): Int {
        if (major != other.major) {
            return major.compareTo(other.major)
        }
        if (minor != other.minor) {
            return minor.compareTo(other.minor)
        }
        return patch.compareTo(other.patch)
    }
}

fun AppVersionRemoteConfig.toDomain() = AppVersion(major = major, minor = minor, patch = patch)

fun AppVersion.toData() = AppVersionRemoteConfig(major = major, minor = minor, patch = patch)
