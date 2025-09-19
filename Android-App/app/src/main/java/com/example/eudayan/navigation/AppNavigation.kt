package com.example.eudayan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
// import androidx.navigation.NavGraph.Companion.findStartDestination // Ensure this or similar is NOT causing issues if old.
//NavController.graph.startDestinationId should be directly available.
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eudayan.auth.AuthViewModel
import com.example.eudayan.auth.LoginScreen
import com.example.eudayan.auth.LoginSelectionScreen
import com.example.eudayan.auth.SignupScreen
import com.example.eudayan.main.MainScreen

@Composable
fun AppNavigation(initialRoute: String? = null) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login_selection") {
        composable("login_selection") {
            LoginSelectionScreen(navController = navController, onSkip = {
                navController.navigate("main") {
                    popUpTo("login_selection") { inclusive = true }
                }
            })
        }
        composable("signup") {
            SignupScreen(navController = navController, viewModel = authViewModel)
        }
        composable(
            "login/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) {
            val bundle = it.arguments
            val role = bundle?.getString("role") ?: ""
            LoginScreen(
                viewModel = authViewModel,
                role = role,
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true } // Changed here
                        launchSingleTop = true
                    }
                }
                // navController = navController // Temporarily removed
            )
        }
        composable("main") {
            MainScreen(onSignOut = {
                navController.navigate("login_selection") {
                    popUpTo("main") { inclusive = true }
                }
            })
        }
    }

    initialRoute?.let { route ->
        LaunchedEffect(route) {
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true } // Changed here
                launchSingleTop = true
            }
        }
    }
}
