package com.prac101.mymenulist_di

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.prac101.mymenulist_di.screens.HomeScreen
import com.prac101.mymenulist_di.screens.LogInScreen
import com.prac101.mymenulist_di.screens.ProfileScreen
import com.prac101.mymenulist_di.screens.RegisterScreen
import com.prac101.mymenulist_di.screens.RegisterSuccessScreen
import com.prac101.mymenulist_di.screens.SplashScreen
import com.prac101.mymenulist_di.ui.theme.MyMenuList_DITheme
import com.prac101.mymenulist_di.viewmodels.HomeViewModel
import com.prac101.mymenulist_di.viewmodels.MainViewModel
import com.prac101.mymenulist_di.viewmodels.RegisterSuccessViewModel
import com.prac101.mymenulist_di.viewmodels.StartDestination
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
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel(),
    registerSuccessViewModel: RegisterSuccessViewModel = viewModel()
    ){
    val navController = rememberNavController()
    val startDestination by mainViewModel.startDestination.collectAsState()

    if(startDestination == StartDestination.SPLASH)
    {
        SplashScreen()
        return
    }
    NavHost(navController = navController, startDestination = startDestination.name.lowercase()) {
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
                    navController.navigate("register") // Navigate to the profile
                },
                onForgotPassword = {
                    // Handle forgot password
                }
            )
        }
        composable("home") {
            HomeScreen(
                onLogout = {
                    // Clear the tokens and navigate to the login screen
                    homeViewModel.clearTokens()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { userEmail ->
                    navController.navigate("registerSuccess/$userEmail")
                },
                onNavigateToLogin = {
                    navController.navigate("login"){
                        popUpTo("login") { inclusive = true }

                    }
                }
            )
        }
        composable("profile") {
            ProfileScreen(
                onBack = {
                    navController.navigate("home")
                }
            )

        }
        composable(
            "registerSuccess/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: "No email provided"
            // Collect the 'isVerified' state as a Compose State object
            val isVerified by registerSuccessViewModel.isVerified.collectAsState()

            // This effect starts the background polling process
            LaunchedEffect(Unit) {
                registerSuccessViewModel.startPollingForVerification(email)
            }


            // This effect handles the automatic navigation once verification is successful
            LaunchedEffect(isVerified) {
                if (isVerified) {
                    // Optional: A small delay so the user sees the button enable before navigating
                    kotlinx.coroutines.delay(500)
                    // Navigate to login...
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }

            RegisterSuccessScreen(
                email = email,
                isVerified = isVerified,
                onNavigateToLogin = {
                    navController.navigate("login"){
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
    }
}