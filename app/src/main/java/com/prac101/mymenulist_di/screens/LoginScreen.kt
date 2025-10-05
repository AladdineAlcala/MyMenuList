package com.prac101.mymenulist_di.screens

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.prac101.mymenulist_di.R
import com.prac101.mymenulist_di.viewmodels.LoginViewModel
import com.prac101.mymenulist_di.common.Result
import com.prac101.mymenulist_di.dto.LoginResponse

@Composable
fun LogInScreen(
    onLoginSuccess: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {}, // Callback to navigate to SignUp
    onForgotPassword: () -> Unit = {} // Callback for forgot password
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val context = LocalContext.current

    // State for UI elements
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var rememberMe by rememberSaveable { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    // State from ViewModel
    val loginState by viewModel.loginState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Handle login result side effects
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is Result.Success -> {
                showToast(context, "Login Successful!")
                // The actual token saving should be in the ViewModel or a repository
                // but we trigger navigation from here.
                onLoginSuccess()
            }
            is Result.Error -> {
                showToast(context, state.message)
            }
            else -> {} // Handle Idle state
        }
    }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 48.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()), // Make the form scrollable
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image( painter = painterResource(id= R.drawable.loginimage),
                contentDescription = "mainImage",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )
            Text(text = "Welcome Back!", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "Sign in to continue",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painterResource(id = image), description)
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- 1 & 2. Remember Me & Forgot Password ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it }
                    )
                    Text("Remember me")
                }
                TextButton(onClick = onForgotPassword) {
                    Text("Forgot Password?")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 4. Login Button ---
            Button(
                onClick = {
                    if (validateInput(context, email, password)) {
                        viewModel.login(email, password, rememberMe)
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = "Log In", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 3. External Sign In ---
            Text("or continue with", color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                SocialLoginIcon(iconId = R.drawable.ic_google) { showToast(context, "Google Sign-in clicked") }
                Spacer(modifier = Modifier.width(24.dp))
                SocialLoginIcon(iconId = R.drawable.ic_facebook) { showToast(context, "Facebook Sign-in clicked") }
                Spacer(modifier = Modifier.width(24.dp))
                SocialLoginIcon(iconId = R.drawable.ic_x) { showToast(context, "Twitter Sign-in clicked") }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- 5. Sign Up Feature ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account?")
                TextButton(onClick = onNavigateToSignUp) {
                    Text("Sign Up", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Helper composable for social media icons
@Composable
fun SocialLoginIcon(iconId: Int, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = iconId),
        contentDescription = "Social Login",
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(8.dp)
    )
}

// Utility functions (assuming you add these icons to your drawable folder)
private fun validateInput(context: Context, email: String, password: String): Boolean {
    // Same as your original
    return when {
        email.isEmpty() -> {
            showToast(context, "📧 Please enter your email address")
            false
        }
        password.isEmpty() -> {
            showToast(context, "🔒 Please enter your password")
            false
        }
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
            showToast(context, "❌ Please enter a valid email address")
            false
        }
        else -> true
    }
}

private fun saveAuthToken(context: Context, token: String?) {
    val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    sharedPref.edit().putString("token", token).apply()
}

private fun showToast(context: Context, message: String?) {
    val toastMessage = message ?: "An error occurred"  // Handle null messages
    if (toastMessage.isNotEmpty()) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }
    // If message is empty, don't show anything
}