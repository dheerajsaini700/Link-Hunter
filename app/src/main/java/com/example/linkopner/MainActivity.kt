package com.example.linkopner

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.linkopner.logic.ScanResult
import com.example.linkopner.logic.UrlScanner
import com.example.linkopner.logic.StatsManager
import com.example.linkopner.ui.screens.*
import com.example.linkopner.ui.sandbox.SecureSandboxScreen
import com.example.linkopner.ui.theme.LinkopnerTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    
    private var initialUrl = mutableStateOf<String?>(null)
    private lateinit var statsManager: StatsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statsManager = StatsManager(this)
        
        handleIntent(intent)
        
        enableEdgeToEdge()
        setContent {
            LinkopnerTheme(darkTheme = true) {
                LinkScrubberApp(initialUrl.value, statsManager)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {
            initialUrl.value = intent.dataString
        }
    }
}

enum class Screen {
    SCANNING, SAFE_RESULT, MALICIOUS_RESULT, SETTINGS, SANDBOX
}

@Composable
fun LinkScrubberApp(passedUrl: String?, statsManager: StatsManager) {
    var currentScreen by remember { mutableStateOf(if (passedUrl != null) Screen.SCANNING else Screen.SETTINGS) }
    var urlToAnalyze by remember { mutableStateOf(passedUrl ?: "") }
    var scanResult by remember { mutableStateOf<ScanResult?>(null) }
    
    var totalScanned by remember { mutableStateOf(statsManager.getScannedCount()) }
    var totalSuspicious by remember { mutableStateOf(statsManager.getSuspiciousCount()) }

    // Manual Scan Trigger from Settings
    val onManualScan: (String) -> Unit = { url ->
        urlToAnalyze = url
        scanResult = null // Reset previous result
        currentScreen = Screen.SCANNING
    }

    // Handle Intent-based URL changes
    LaunchedEffect(passedUrl) {
        if (passedUrl != null && passedUrl != urlToAnalyze) {
            urlToAnalyze = passedUrl
            scanResult = null // Reset previous result
            currentScreen = Screen.SCANNING
        }
    }

    // The Scanning Process
    LaunchedEffect(currentScreen, urlToAnalyze) {
        if (currentScreen == Screen.SCANNING && urlToAnalyze.isNotEmpty() && scanResult == null) {
            delay(1200) // Slightly longer to feel more realistic
            val result = UrlScanner.scan(urlToAnalyze)
            
            // Update Real Stats
            statsManager.incrementScanned()
            totalScanned = statsManager.getScannedCount()
            if (!result.isSafe) {
                statsManager.incrementSuspicious()
                totalSuspicious = statsManager.getSuspiciousCount()
            }
            
            scanResult = result
            currentScreen = if (result.isSafe) Screen.SAFE_RESULT else Screen.MALICIOUS_RESULT
        }
    }

    when (currentScreen) {
        Screen.SCANNING -> {
            ScanningScreen(url = urlToAnalyze)
        }
        Screen.SAFE_RESULT -> {
            SafeResultScreen(
                url = urlToAnalyze,
                onOpenInChrome = {
                    currentScreen = Screen.SANDBOX 
                }
            )
        }
        Screen.MALICIOUS_RESULT -> {
            MaliciousResultScreen(
                url = urlToAnalyze,
                onGoBack = {
                    currentScreen = Screen.SETTINGS
                },
                onProceed = {
                    currentScreen = Screen.SANDBOX
                }
            )
        }
        Screen.SETTINGS -> {
            SettingsDashboardScreen(
                onScanUrl = onManualScan,
                scannedCount = totalScanned,
                suspiciousCount = totalSuspicious
            )
        }
        Screen.SANDBOX -> {
            SecureSandboxScreen(
                url = urlToAnalyze,
                onBack = {
                    currentScreen = Screen.SETTINGS
                }
            )
        }
    }
}
