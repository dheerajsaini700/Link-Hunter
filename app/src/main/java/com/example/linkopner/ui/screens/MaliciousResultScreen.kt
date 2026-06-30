package com.example.linkopner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.linkopner.ui.theme.LinkopnerTheme
import com.example.linkopner.ui.theme.BrightCrimson

@Composable
fun MaliciousResultScreen(url: String, onGoBack: () -> Unit, onProceed: () -> Unit) {
    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onGoBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrightCrimson, contentColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Go Back to Safety", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                
                TextButton(
                    onClick = onProceed,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Inspect in Secure Sandbox (Isolated)", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
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
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                modifier = Modifier.size(80.dp),
                tint = BrightCrimson
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Security Warning",
                color = BrightCrimson,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = BrightCrimson.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BrightCrimson.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Threat Detected",
                        style = MaterialTheme.typography.labelLarge,
                        color = BrightCrimson,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = "Homograph attack detected: g00gle.com instead of google.com. This website is attempting to impersonate another service to steal your information.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = url,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MaliciousResultScreenPreview() {
    LinkopnerTheme(darkTheme = true) {
        MaliciousResultScreen(
            url = "https://g00gle.com/login",
            onGoBack = {},
            onProceed = {}
        )
    }
}
