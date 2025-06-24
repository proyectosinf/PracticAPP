package com.mobivery.fct25.data.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class AppVersionTests {

    @Test
    fun `test constructor with string version`() {
        // GIVEN
        val version = AppVersionRemoteConfig("1.2.3")

        // THEN
        assertEquals(1, version.major)
        assertEquals(2, version.minor)
        assertEquals(3, version.patch)
    }

    @Test
    fun `test toString method`() {
        // GIVEN
        val version = AppVersionRemoteConfig(1, 2, 3)

        // THEN
        assertEquals("1.2.3", version.toString())
    }

    @Test
    fun `test compareTo method with different major`() {
        // GIVEN
        val version1 = AppVersionRemoteConfig(2, 0, 0)
        val version2 = AppVersionRemoteConfig(1, 9, 9)

        // THEN
        assert(version1 > version2)
    }

    @Test
    fun `test compareTo method with different minor`() {
        // GIVEN
        val version1 = AppVersionRemoteConfig(1, 2, 0)
        val version2 = AppVersionRemoteConfig(1, 1, 9)

        // THEN
        assert(version1 > version2)
    }

    @Test
    fun `test compareTo method with different patch`() {
        // GIVEN
        val version1 = AppVersionRemoteConfig(1, 1, 2)
        val version2 = AppVersionRemoteConfig(1, 1, 1)

        // THEN
        assert(version1 > version2)
    }

    @Test
    fun `test compareTo method with equal versions`() {
        // GIVEN
        val version1 = AppVersionRemoteConfig(1, 1, 1)
        val version2 = AppVersionRemoteConfig(1, 1, 1)

        // THEN
        assertEquals(version1, version2)
    }

    @Test
    fun `test compareTo method with all different`() {
        // GIVEN
        val version1 = AppVersionRemoteConfig(2, 0, 0)
        val version2 = AppVersionRemoteConfig(1, 2, 3)

        // THEN
        assert(version1 > version2)
    }
}