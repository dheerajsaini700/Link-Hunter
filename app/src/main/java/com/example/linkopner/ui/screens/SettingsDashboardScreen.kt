package com.example.linkopner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.linkopner.R
import com.example.linkopner.ui.theme.NeonGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDashboardScreen(
    onScanUrl: (String) -> Unit, 
    scannedCount: Int, 
    suspiciousCount: Int,
    neutralizedCount: Int
) {
    var autoScan by remember { mutableStateOf(true) }
    var blockIps by remember { mutableStateOf(true) }
    var manualUrl by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_linkhunter_logo),
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "LINKHUNTER", 
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = NeonGreen
                )
            )
        },
        containerColor = Color(0xFF0A0A0A)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // 3D Styled Header Card for Manual Scan
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(24.dp), ambientColor = NeonGreen, spotColor = NeonGreen)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF1A1A1A), Color(0xFF0F0F0F))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "TARGET ACQUISITION",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonGreen.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = manualUrl,
                        onValueChange = { manualUrl = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter suspicious URL...", color = Color.Gray) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = Color.DarkGray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.LightGray
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { if (manualUrl.isNotBlank()) onScanUrl(manualUrl) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(8.dp, RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("INITIATE SCAN", fontWeight = FontWeight.Black, fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Professional Statistics Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SmallStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Total Scans",
                    value = scannedCount.toString(),
                    icon = Icons.Default.Analytics
                )
                SmallStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Threats Detected",
                    value = suspiciousCount.toString(),
                    icon = Icons.Default.Shield,
                    accentColor = if (suspiciousCount > 0) Color.Red else Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SYSTEM CONFIGURATION",
                style = MaterialTheme.typography.labelSmall,
                color = NeonGreen,
                modifier = Modifier.padding(start = 4.dp, bottom = 12.dp),
                fontWeight = FontWeight.Bold
            )

            // Modern Toggle Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF121212))
                    .padding(8.dp)
            ) {
                SettingsToggle(
                    title = "Automatic Interception",
                    description = "Hunt threats in real-time",
                    checked = autoScan,
                    onCheckedChange = { autoScan = it }
                )
                Divider(color = Color.DarkGray.copy(alpha = 0.3f), modifier = Modifier.padding(horizontal = 16.dp))
                SettingsToggle(
                    title = "IP Blacklist active",
                    description = "Block known malicious servers",
                    checked = blockIps,
                    onCheckedChange = { blockIps = it }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // History Card
            StatsCard(
                icon = Icons.Default.History,
                title = "Recent Hunter Activity",
                value = "View Full Logs",
                trend = "$neutralizedCount threats neutralized today"
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SmallStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    accentColor: Color = NeonGreen
) {
    Box(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1A1A1A))
            .padding(16.dp)
    ) {
        Column {
            Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@Composable
fun SettingsToggle(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = NeonGreen,
                uncheckedThumbColor = Color.DarkGray,
                uncheckedTrackColor = Color(0xFF2A2A2A)
            )
        )
    }
}

@Composable
fun StatsCard(icon: ImageVector, title: String, value: String, trend: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = NeonGreen.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = NeonGreen)
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(text = title, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = NeonGreen)
                Text(text = trend, style = MaterialTheme.typography.labelSmall, color = NeonGreen.copy(alpha = 0.7f))
            }
        }
    }
}
