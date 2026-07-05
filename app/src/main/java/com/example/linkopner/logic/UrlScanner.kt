package com.example.linkopner.logic

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class ScanResult(
    val url: String,
    val isSafe: Boolean,
    val threatType: String? = null,
    val threatDescription: String? = null,
    val maliciousProbability: Int,
    val domain: String,
    // VirusTotal specific data
    val vtMalicious: Int = 0,
    val vtSuspicious: Int = 0,
    val vtHarmless: Int = 0,
    val vtUndetected: Int = 0,
    val isVtVerified: Boolean = false
)

object UrlScanner {
    // NOTE: You should ideally move this to a secure place or BuildConfig
    private const val VT_API_KEY = "YOUR_VIRUSTOTAL_API_KEY_HERE"
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val SUSPICIOUS_KEYWORDS = listOf("login", "verify", "secure", "update", "bank", "account", "signin")

    suspend fun scan(url: String): ScanResult = withContext(Dispatchers.IO) {
        val uri = Uri.parse(url)
        var host = uri.host?.lowercase() ?: ""
        
        if (host.isEmpty()) {
            host = url.substringBefore("/").substringAfter("://").lowercase()
        }
        
        val displayDomain = if (host.isNotEmpty()) host else "Unknown Domain"
        var localScore = 0
        var localThreatType: String? = null
        var localThreatDescription: String? = null

        // 1. Local Heuristics (Instant)
        if (host.contains("g00gle") || host.contains("paypa1") || host.contains("fac3book")) {
            localScore += 80
            localThreatType = "Homograph Attack"
            localThreatDescription = "This link uses characters that look like another website (e.g., '0' instead of 'o') to trick you."
        }

        val pathAndQuery = (uri.path ?: "") + (uri.query ?: "")
        val foundKeywords = SUSPICIOUS_KEYWORDS.filter { pathAndQuery.contains(it, ignoreCase = true) }
        if (foundKeywords.isNotEmpty()) {
            localScore += foundKeywords.size * 10
            if (localThreatType == null) {
                localThreatType = "Suspicious Content"
                localThreatDescription = "This link contains keywords often used in phishing attacks."
            }
        }

        // 2. VirusTotal Integration (Real API call)
        var vtMalicious = 0
        var vtSuspicious = 0
        var vtHarmless = 0
        var vtUndetected = 0
        var isVtVerified = false

        if (VT_API_KEY != "YOUR_VIRUSTOTAL_API_KEY_HERE") {
            try {
                // Submit URL to get scan ID
                val formBody = FormBody.Builder().add("url", url).build()
                val request = Request.Builder()
                    .url("https://www.virustotal.com/api/v3/urls")
                    .addHeader("x-apikey", VT_API_KEY)
                    .post(formBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val json = JSONObject(response.body?.string() ?: "")
                        val analysisId = json.getJSONObject("data").getString("id")
                        
                        // Poll for results (Simplified: we wait once)
                        java.lang.Thread.sleep(2000)
                        
                        val resultRequest = Request.Builder()
                            .url("https://www.virustotal.com/api/v3/analyses/$analysisId")
                            .addHeader("x-apikey", VT_API_KEY)
                            .get()
                            .build()
                            
                        client.newCall(resultRequest).execute().use { res ->
                            if (res.isSuccessful) {
                                val resJson = JSONObject(res.body?.string() ?: "")
                                val stats = resJson.getJSONObject("data")
                                    .getJSONObject("attributes")
                                    .getJSONObject("stats")
                                
                                vtMalicious = stats.getInt("malicious")
                                vtSuspicious = stats.getInt("suspicious")
                                vtHarmless = stats.getInt("harmless")
                                vtUndetected = stats.getInt("undetected")
                                isVtVerified = true
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Final Logic: Combine Local and VirusTotal
        val totalMaliciousSignal = vtMalicious + vtSuspicious + (if (localScore >= 50) 1 else 0)
        val isSafe = totalMaliciousSignal == 0
        
        if (!isSafe && localThreatType == null) {
            localThreatType = "Malware/Phishing Detected"
            localThreatDescription = "Security engines on VirusTotal have flagged this link as dangerous."
        }

        ScanResult(
            url = url,
            isSafe = isSafe,
            threatType = localThreatType,
            threatDescription = localThreatDescription,
            maliciousProbability = if (isSafe) 0 else 100,
            domain = displayDomain,
            vtMalicious = vtMalicious,
            vtSuspicious = vtSuspicious,
            vtHarmless = vtHarmless,
            vtUndetected = vtUndetected,
            isVtVerified = isVtVerified
        )
    }
}
