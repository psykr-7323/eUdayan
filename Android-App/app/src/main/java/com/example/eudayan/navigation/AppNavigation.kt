package com.example.eudayan.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
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
fun AppNavigation() {
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
            val role = it.arguments?.getString("role") ?: ""
            LoginScreen(
                navController = navController,
                viewModel = authViewModel,
                role = role,
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
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
}
