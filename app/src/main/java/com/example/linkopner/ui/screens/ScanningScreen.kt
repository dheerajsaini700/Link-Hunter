package com.example.linkopner.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.linkopner.ui.theme.NeonGreen

@Composable
fun ScanningScreen(url: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050505)) // Deep Space Black
    ) {
        // Immersive Background Grid/Effect could be added here
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "NEURAL LINK ANALYSIS",
                style = MaterialTheme.typography.labelMedium,
                color = NeonGreen,
                letterSpacing = 4.sp,
                fontWeight = FontWeight.Black
            )
            
            Spacer(modifier = Modifier.height(48.dp))

            RadarAnimation()

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "DECRYPTING TARGET...",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Professional "URL Data Box"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF121212))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "ENCRYPTED URL STRING",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = url,
                        style = MaterialTheme.typography.bodySmall,
                        color = NeonGreen.copy(alpha = 0.8f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Scanning Progress indicator
            LinearProgressIndicator()
        }
    }
}

@Composable
fun LinearProgressIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "progress")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress_anim"
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .height(2.dp)
            .clip(RoundedCornerShape(1.dp))
            .background(Color.DarkGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .background(NeonGreen)
        )
    }
}

@Composable
fun RadarAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1250, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
        Canvas(modifier = Modifier.size(200.dp)) {
            // Background Circles
            drawCircle(
                color = NeonGreen.copy(alpha = 0.05f),
                radius = (size.minDimension / 2) * scale,
                style = Stroke(width = 1f)
            )
            drawCircle(
                color = NeonGreen.copy(alpha = 0.1f),
                radius = size.minDimension / 2,
                style = Stroke(width = 2f)
            )
            drawCircle(
                color = NeonGreen.copy(alpha = 0.2f),
                radius = size.minDimension / 3,
                style = Stroke(width = 1f)
            )

            // The Sweep
            val brush = Brush.sweepGradient(
                0f to Color.Transparent,
                0.8f to Color.Transparent,
                1f to NeonGreen.copy(alpha = 0.6f),
                center = center
            )
            
            drawArc(
                brush = brush,
                startAngle = rotation,
                sweepAngle = 120f,
                useCenter = true
            )
            
            // Pulse at end of sweep
            drawArc(
                color = NeonGreen,
                startAngle = rotation + 120f,
                sweepAngle = 2f,
                useCenter = true,
                style = Stroke(width = 4f)
            )
        }
        
        // Center Target Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(NeonGreen.copy(alpha = 0.1f), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("+", color = NeonGreen, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}
