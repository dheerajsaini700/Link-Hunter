package com.example.linkopner.logic

import android.net.Uri

data class ScanResult(
    val url: String,
    val isSafe: Boolean,
    val threatType: String? = null,
    val threatDescription: String? = null,
    val maliciousProbability: Int,
    val domain: String
)

object UrlScanner {
    private val SUSPICIOUS_KEYWORDS = listOf("login", "verify", "secure", "update", "bank", "account", "signin")
    private val HOMOGRAPH_MAP = mapOf(
        '0' to 'o',
        '1' to 'l',
        '5' to 's',
        'q' to 'g',
        'v' to 'u'
    )

    fun scan(url: String): ScanResult {
        val uri = Uri.parse(url)
        val host = uri.host?.lowercase() ?: ""
        var score = 0
        var threatType: String? = null
        var threatDescription: String? = null

        // 1. Homograph Detection (Simple demo version)
        if (host.contains("g00gle") || host.contains("paypa1") || host.contains("fac3book")) {
            score += 80
            threatType = "Homograph Attack"
            threatDescription = "This link uses characters that look like another website (e.g., '0' instead of 'o') to trick you."
        }

        // 2. Suspicious Keywords in Path/Query
        val pathAndQuery = (uri.path ?: "") + (uri.query ?: "")
        val foundKeywords = SUSPICIOUS_KEYWORDS.filter { pathAndQuery.contains(it, ignoreCase = true) }
        if (foundKeywords.isNotEmpty()) {
            score += foundKeywords.size * 10
            if (threatType == null) {
                threatType = "Suspicious Content"
                threatDescription = "This link contains keywords often used in phishing attacks: ${foundKeywords.joinToString(", ")}."
            }
        }

        // 3. IP Address instead of Domain
        val ipRegex = """^(\d{1,3}\.){3}\d{1,3}$""".toRegex()
        if (ipRegex.matches(host)) {
            score += 40
            if (threatType == null) {
                threatType = "IP-Based URL"
                threatDescription = "Most legitimate sites use names, not numbers. IP addresses are often used to hide the site's true identity."
            }
        }

        // Final Safety Determination
        val isSafe = score < 50
        
        return ScanResult(
            url = url,
            isSafe = isSafe,
            threatType = threatType,
            threatDescription = threatDescription,
            maliciousProbability = score.coerceAtMost(100),
            domain = host
        )
    }
}
