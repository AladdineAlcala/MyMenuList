package com.prac101.mymenulist_di.screens

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import com.prac101.mymenulist_di.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    // You would create a RegisterViewModel for this screen's logic
    // val viewModel: RegisterViewModel = hiltViewModel()
    val context = LocalContext.current

    // State for UI elements
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    // Mock state from a potential ViewModel
    val isLoading by remember { mutableStateOf(false) }
    // val registerState by viewModel.registerState.collectAsState()

    /*
    // Handle registration result side effects
    LaunchedEffect(registerState) {
        when (val state = registerState) {
            is Result.Success -> {
                showToast(context, "Registration Successful!")
                onRegisterSuccess()
            }
            is Result.Error -> {
                showToast(context, state.message)
            }
            else -> {} // Handle Idle state
        }
    }
    */

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
            Text(text = "Create Account", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "Let's get you started!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- 1. Username, Email, Password Fields ---

            // Username Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(24.dp))

            // --- Register Button ---
            Button(
                onClick = {
                    // --- 2. Validation Feature ---
                    if (validateRegistrationInput(context, username, email, password)) {
                        // This is where you would call your ViewModel
                        // viewModel.register(username, email, password)
                        showToast(context, "Registration logic goes here...")
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
                    Text(text = "Sign Up", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Navigation to Login Screen ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Already have an account?")
                TextButton(onClick = onNavigateToLogin) {
                    Text("Log In", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

}

// Re-using the showToast function from your project
private fun showToast(context: Context, message: String?) {
    val toastMessage = message ?: "An error occurred"
    if (toastMessage.isNotEmpty()) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }
}

private fun validateRegistrationInput(
    context: Context,
    username: String,
    email: String,
    password: String
): Boolean {
    return when {
        username.isEmpty() -> {
            showToast(context, "👤 Please enter a username")
            false
        }
        email.isEmpty() -> {
            showToast(context, "📧 Please enter your email address")
            false
        }
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
            showToast(context, "❌ Please enter a valid email address")
            false
        }
        password.isEmpty() -> {
            showToast(context, "🔒 Please enter your password")
            false
        }
        password.length < 8 -> {
            showToast(context, "⚠️ Password must be at least 8 characters long")
            false
        }
        else -> true
    }
}