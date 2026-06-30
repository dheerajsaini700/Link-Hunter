package com.example.linkopner

import com.example.linkopner.logic.UrlScanner
import org.junit.Assert.*
import org.junit.Test

class UrlScannerTest {

    @Test
    fun `scan safe url returns safe result`() {
        val result = UrlScanner.scan("https://www.google.com")
        assertTrue(result.isSafe)
        assertEquals(0, result.maliciousProbability)
    }

    @Test
    fun `scan homograph url returns malicious result`() {
        val result = UrlScanner.scan("https://g00gle.com/login")
        assertFalse(result.isSafe)
        assertEquals("Homograph Attack", result.threatType)
        assertTrue(result.maliciousProbability >= 80)
    }

    @Test
    fun `scan suspicious keyword url increases probability`() {
        val result = UrlScanner.scan("https://safe-site.com/verify/account/update")
        // It might still be "safe" (score < 50) but probability should be > 0
        assertTrue(result.maliciousProbability > 0)
        assertEquals("Suspicious Content", result.threatType)
    }

    @Test
    fun `scan IP based url increases probability`() {
        val result = UrlScanner.scan("http://192.168.1.1/admin")
        assertEquals("IP-Based URL", result.threatType)
        assertTrue(result.maliciousProbability >= 40)
    }
}
