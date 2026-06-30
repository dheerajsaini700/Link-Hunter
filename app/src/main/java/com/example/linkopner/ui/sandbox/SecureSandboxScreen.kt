package com.example.linkopner.ui.sandbox

import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.linkopner.ui.theme.NeonGreen
import com.example.linkopner.ui.theme.BrightCrimson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecureSandboxScreen(url: String, onBack: () -> Unit) {
    var interceptedPermission by remember { mutableStateOf<String?>(null) }
    var showPermissionWarning by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Secure Sandbox", style = MaterialTheme.typography.titleMedium)
                            Text(url, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Icon(Icons.Default.Security, contentDescription = "Protected", tint = NeonGreen, modifier = Modifier.padding(end = 16.dp))
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = android.view.ViewGroup.LayoutParams(
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            databaseEnabled = true
                            loadWithOverviewMode = true
                            useWideViewPort = true
                            builtInZoomControls = true
                            displayZoomControls = false
                            
                            allowFileAccess = true // Some modern sites need this for blob URLs
                            allowContentAccess = true
                            
                            mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            
                            userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                        }
                        
                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                            }

                            override fun onReceivedError(view: WebView?, request: android.webkit.WebResourceRequest?, error: android.webkit.WebResourceError?) {
                                super.onReceivedError(view, request, error)
                                // Debug log could go here
                            }
                            
                            override fun shouldOverrideUrlLoading(view: WebView?, request: android.webkit.WebResourceRequest?): Boolean {
                                return false
                            }
                        }
                        
                        webChromeClient = object : WebChromeClient() {
                            override fun onPermissionRequest(request: PermissionRequest) {
                                interceptedPermission = request.resources.joinToString(", ")
                                showPermissionWarning = true
                                request.deny() 
                            }
                        }
                        
                        loadUrl(url)
                    }
                },
                update = { webView ->
                    if (webView.url != url) {
                        webView.loadUrl(url)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if (showPermissionWarning) {
                AlertDialog(
                    onDismissRequest = { showPermissionWarning = false },
                    icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = BrightCrimson) },
                    title = { Text("Permission Blocked") },
                    text = {
                        Text("The website tried to access: $interceptedPermission\n\nLink Scrubber has blocked this request to prevent data leakage from your camera, microphone, or files.")
                    },
                    confirmButton = {
                        TextButton(onClick = { showPermissionWarning = false }) {
                            Text("I Understand")
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(28.dp)
                )
            }
            
            // Safety Banner
            Surface(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                color = NeonGreen.copy(alpha = 0.9f)
            ) {
                Text(
                    text = "Shield Active: All hardware permissions are isolated and blocked.",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
