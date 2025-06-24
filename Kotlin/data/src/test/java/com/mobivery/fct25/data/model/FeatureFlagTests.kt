package com.mobivery.fct25.data.model

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class FeatureFlagTests {

    @Test
    fun `test isActive returns false when isActive is null`() {
        // GIVEN
        val featureFlag = FeatureFlagRemoteConfig(
            name = "test",
            isActive = null,
            fromVersion = AppVersionRemoteConfig("1.0.0"),
            toVersion = AppVersionRemoteConfig("2.0.0")
        )

        // THEN
        assertFalse(featureFlag.isActive(AppVersionRemoteConfig("1.5.0")))
    }

    @Test
    fun `test isActive returns false when fromVersion is null`() {
        // GIVEN
        val featureFlag = FeatureFlagRemoteConfig(
            name = "test",
            isActive = true,
            fromVersion = null,
            toVersion = AppVersionRemoteConfig("2.0.0")
        )

        // THEN
        assertFalse(featureFlag.isActive(AppVersionRemoteConfig("1.5.0")))
    }

    @Test
    fun `test isActive returns false when toVersion is null`() {
        // GIVEN
        val featureFlag = FeatureFlagRemoteConfig(
            name = "test",
            isActive = true,
            fromVersion = AppVersionRemoteConfig("1.0.0"),
            toVersion = null
        )

        // THEN
        assertFalse(featureFlag.isActive(AppVersionRemoteConfig("1.5.0")))
    }

    @Test
    fun `test isActive returns false when version is below fromVersion`() {
        // GIVEN
        val featureFlag = FeatureFlagRemoteConfig(
            name = "test",
            isActive = true,
            fromVersion = AppVersionRemoteConfig("1.0.0"),
            toVersion = AppVersionRemoteConfig("2.0.0")
        )

        // THEN
        assertFalse(featureFlag.isActive(AppVersionRemoteConfig("0.9.0")))
    }

    @Test
    fun `test isActive returns false when version is above toVersion`() {
        // GIVEN
        val featureFlag = FeatureFlagRemoteConfig(
            name = "test",
            isActive = true,
            fromVersion = AppVersionRemoteConfig("1.0.0"),
            toVersion = AppVersionRemoteConfig("2.0.0")
        )

        // THEN
        assertFalse(featureFlag.isActive(AppVersionRemoteConfig("2.1.0")))
    }

    @Test
    fun `test isActive returns true when version is the same as fromVersion`() {
        // GIVEN
        val featureFlag = FeatureFlagRemoteConfig(
            name = "test",
            isActive = true,
            fromVersion = AppVersionRemoteConfig("1.0.0"),
            toVersion = AppVersionRemoteConfig("2.0.0")
        )

        // THEN
        assertTrue(featureFlag.isActive(AppVersionRemoteConfig("1.0.0")))
    }

    @Test
    fun `test isActive returns true when version is the same as toVersion`() {
        // GIVEN
        val featureFlag = FeatureFlagRemoteConfig(
            name = "test",
            isActive = true,
            fromVersion = AppVersionRemoteConfig("1.0.0"),
            toVersion = AppVersionRemoteConfig("2.0.0")
        )

        // THEN
        assertTrue(featureFlag.isActive(AppVersionRemoteConfig("2.0.0")))
    }

    @Test
    fun `test isActive returns true when version is within range`() {
        // GIVEN
        val featureFlag = FeatureFlagRemoteConfig(
            name = "test",
            isActive = true,
            fromVersion = AppVersionRemoteConfig("1.0.0"),
            toVersion = AppVersionRemoteConfig("2.0.0")
        )

        // THEN
        assertTrue(featureFlag.isActive(AppVersionRemoteConfig("1.5.0")))
    }

    @Test
    fun `test isActive returns false when isActive is false`() {
        // GIVEN
        val featureFlag = FeatureFlagRemoteConfig(
            name = "test",
            isActive = false,
            fromVersion = AppVersionRemoteConfig("1.0.0"),
            toVersion = AppVersionRemoteConfig("2.0.0")
        )

        // THEN
        assertFalse(featureFlag.isActive(AppVersionRemoteConfig("1.5.0")))
    }
}