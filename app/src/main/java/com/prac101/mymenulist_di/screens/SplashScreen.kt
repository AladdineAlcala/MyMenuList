package com.prac101.mymenulist_di.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prac101.mymenulist_di.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    // We add a callback to notify when the splash screen is done
    // This is useful if you want to time it with other loading tasks
    onFinished: (() -> Unit)? = null
) {
    // This state will control the animation's progress.
    // We'll start with the UI being invisible and small (start = true).
    var startAnimation by remember { mutableStateOf(false) }

    // 'animateFloatAsState' is a powerful tool. It smoothly animates a float value.
    // When `startAnimation` becomes true, this alpha value will animate from 0f to 1f.
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1500 // Animation duration: 1.5 seconds
        ),
        label = "alphaAnimation"
    )

    // A 'LaunchedEffect' runs a coroutine when the composable first appears.
    // We use it to trigger our animation after a short delay.
    LaunchedEffect(key1 = true) {
        startAnimation = true // Start the animation
        delay(2500) // Wait for 2.5 seconds (animation + loading time)
        onFinished?.invoke() // Notify the caller that the splash screen is done
    }

    // --- The UI of our Splash Screen ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            // Use the primary container color from your theme for a beautiful background
            .background(MaterialTheme.colorScheme.primaryContainer),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // The Icon's alpha is tied to our animation state.
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_background), // Your app's logo
            contentDescription = "App Logo",
            modifier = Modifier
                .size(120.dp)
                .alpha(alphaAnim), // Apply the fade-in animation
            tint = MaterialTheme.colorScheme.onPrimaryContainer // Use a color that contrasts well
        )

        Spacer(modifier = Modifier.height(24.dp))

        // App name text, also animated
        Text(
            text = "DaVinci v1.0",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.alpha(alphaAnim) // Apply the fade-in animation
        )

        Spacer(modifier = Modifier.height(48.dp))

        // The loading indicator
        CircularProgressIndicator(
            modifier = Modifier
                .size(32.dp)
                .alpha(alphaAnim), // Also fades in
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            strokeWidth = 3.dp
        )
    }
}