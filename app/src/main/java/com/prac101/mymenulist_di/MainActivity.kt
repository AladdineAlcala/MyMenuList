package com.prac101.mymenulist_di

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prac101.mymenulist_di.screens.HomeScreen
import com.prac101.mymenulist_di.screens.LogInScreen
import com.prac101.mymenulist_di.screens.RegisterScreen
import com.prac101.mymenulist_di.ui.theme.MyMenuList_DITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMenuList_DITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LogInScreen(
                                onLoginSuccess = {
                                    // This part is also important: when a user logs in,
                                    // you must save the new token!
                                    // This would be handled inside your LoginViewModel/Screen
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToSignUp = {
                                    navController.navigate("signup") // Navigate to the profile
                                },
                                onForgotPassword = {
                                    // Handle forgot password
                                }
                            )
                        }
                        composable("home") {
                            HomeScreen()
                        }
                        composable("signup") {
                            RegisterScreen(
                                onRegisterSuccess = {
                                    navController.navigate("login") {
                                        popUpTo("signup") { inclusive = true }
                                    }
                                },
                                onNavigateToLogin = {
                                    navController.navigate("login")
                                }
                            )
                        }
                    }
                }
            }

        }
    }
}
