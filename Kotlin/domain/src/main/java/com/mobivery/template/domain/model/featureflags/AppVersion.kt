package com.mobivery.fct25.domain.model.featureflags

data class AppVersion(val major: Int, val minor: Int, val patch: Int) : Comparable<AppVersion> {

    constructor(version: String) : this(
        version.substringBefore('.').toInt(),
        version.substringAfter('.').substringBefore('.').toInt(),
        version.substringAfterLast('.').toInt()
    )

    override fun toString(): String {
        return "$major.$minor.$patch"
    }

    override fun compareTo(other: AppVersion): Int {
        if (major != other.major) {
            return major.compareTo(other.major)
        }
        if (minor != other.minor) {
            return minor.compareTo(other.minor)
        }
        return patch.compareTo(other.patch)
    }
}
