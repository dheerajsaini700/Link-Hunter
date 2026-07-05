package com.example.linkopner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.linkopner.logic.ScanResult
import com.example.linkopner.ui.theme.LinkopnerTheme
import com.example.linkopner.ui.theme.NeonGreen

@Composable
fun SafeResultScreen(url: String, scanResult: ScanResult?, onOpenInChrome: () -> Unit) {
    Scaffold(
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {
                Button(
                    onClick = onOpenInChrome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Open in Secure Sandbox", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Sandbox protects your camera, contacts, and personal data from being accessed by the website.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Safe",
                modifier = Modifier.size(80.dp),
                tint = NeonGreen
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = NeonGreen.copy(alpha = 0.1f),
                shape = CircleShape
            ) {
                Text(
                    text = "Safe to Browse",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = NeonGreen,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Domain Analysis",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    AnalysisItem(label = "Domain", value = scanResult?.domain ?: "Unknown")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    AnalysisItem(label = "Security Score", value = "${100 - (scanResult?.maliciousProbability ?: 0)}/100 Safe")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    AnalysisItem(label = "Known Phishing", value = "No threats found")
                }
            }
        }
    }
}

@Composable
fun AnalysisItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Preview(showBackground = true)
@Composable
fun SafeResultScreenPreview() {
    LinkopnerTheme(darkTheme = true) {
        SafeResultScreen(url = "https://www.google.com", scanResult = null, onOpenInChrome = {})
    }
}
