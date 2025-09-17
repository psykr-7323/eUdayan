package com.example.eudayan.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eudayan.R
import com.example.eudayan.booking.BookingScreen
import com.example.eudayan.booking.Doctor
import com.example.eudayan.booking.DoctorDetailScreen
import com.example.eudayan.chat.ChatScreen
import com.example.eudayan.community.CommunityViewModel
import com.example.eudayan.community.AddPostScreen
import com.example.eudayan.community.CommunityScreen
import com.example.eudayan.community.PostDetailScreen
import com.example.eudayan.community.RepliesScreen
import com.example.eudayan.home.HomeScreen
import com.example.eudayan.mood.MoodLogScreen
import kotlinx.coroutines.launch

sealed class BottomBarScreen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Home : BottomBarScreen("home", "Home", Icons.Default.Home)
    object Community : BottomBarScreen("community", "Community", Icons.Default.Forum)
    object Booking : BottomBarScreen("booking", "Booking", Icons.Filled.Book)
    object MoodLog : BottomBarScreen("mood_log", "Mood Log")
    object PostDetail : BottomBarScreen("post_detail", "Post Detail")
    object Replies : BottomBarScreen("replies", "Replies")
    object AddPost : BottomBarScreen("add_post", "Add Post")
    object DoctorDetail : BottomBarScreen("doctor_detail", "Doctor Detail")
    object Chat : BottomBarScreen("chat", "Chat")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onSignOut: () -> Unit, showSignupSuccess: Boolean = false) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Community,
        BottomBarScreen.Booking,
    )

    val doctors = listOf(
        Doctor(1, "Dr. Aafreen Khan", 4.9, R.drawable.ic_launcher_background, "Dr. Aafreen Khan is a compassionate and dedicated professional with over 10 years of experience in mental health and wellness. She specializes in cognitive-behavioral therapy and mindfulness practices."),
        Doctor(2, "Dr. Sameer Wani", 4.8, R.drawable.ic_launcher_background, "Dr. Sameer Wani is a clinical psychologist with a focus on adolescent and young adult mental health. He is known for his approachable and empathetic nature, making it easy for patients to open up and share their concerns."),
        Doctor(3, "Dr. Nida Shah", 4.5, R.drawable.ic_launcher_background, "Dr. Nida Shah is a seasoned psychiatrist with a passion for helping individuals achieve mental and emotional balance. She combines medication management with holistic therapies to provide comprehensive care."),
        Doctor(4, "Dr. Imran Bhat", 4.2, R.drawable.ic_launcher_background, "Dr. Imran Bhat is a renowned expert in addiction and recovery. He has helped countless individuals overcome substance abuse and build a foundation for a healthier, more fulfilling life.")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.route in screens.map { it.route } || currentDestination?.route == BottomBarScreen.MoodLog.route
    val showTopBar = currentDestination?.route != BottomBarScreen.Chat.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                AppDrawer(
                    onSignOut = onSignOut,
                    onMoodLog = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate(BottomBarScreen.MoodLog.route)
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (showTopBar) {
                    TopAppBar(
                        title = { Text("Eudayan") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        screens.forEach { screen ->
                            screen.icon?.let {
                                NavigationBarItem(
                                    label = { Text(screen.title) },
                                    icon = { Icon(it, contentDescription = screen.title) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                if (showBottomBar) {
                    when (currentDestination?.route) {
                        BottomBarScreen.Community.route -> {
                            FloatingActionButton(onClick = { navController.navigate(BottomBarScreen.AddPost.route) }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Post")
                            }
                        }
                        BottomBarScreen.Home.route -> {
                            FloatingActionButton(onClick = { navController.navigate(BottomBarScreen.Chat.route) }) {
                                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat")
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BottomBarScreen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(BottomBarScreen.Home.route) { HomeScreen(showSignupSuccess = showSignupSuccess) }
                composable(BottomBarScreen.Community.route) {
                    // You would typically inject this with Hilt or another DI framework
                    val communityViewModel = remember { CommunityViewModel() }
                    CommunityScreen(navController, communityViewModel)
                }
                composable(BottomBarScreen.Booking.route) { BookingScreen(navController, doctors) }
                composable("${BottomBarScreen.PostDetail.route}/{postId}") { backStackEntry ->
                    val postId = backStackEntry.arguments?.getString("postId")
                    if (postId != null) {
                        // PostDetailScreen will need to be refactored to take a postId
                        // and use a ViewModel to fetch the specific post's details.
                    }
                }
                composable("${BottomBarScreen.Replies.route}/{commentLikes}") { backStackEntry ->
                    val commentLikes = backStackEntry.arguments?.getString("commentLikes")?.toInt() ?: 0
                    RepliesScreen(navController, commentLikes)
                }
                composable(BottomBarScreen.AddPost.route) {
                    AddPostScreen(navController) { text ->
                        // This should now call a ViewModel function to create a post via the API.
                    }
                }
                composable(
                    route = "${BottomBarScreen.DoctorDetail.route}/{doctorId}",
                    arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val doctorId = backStackEntry.arguments?.getInt("doctorId")
                    val doctor = doctors.find { it.id == doctorId }
                    if (doctor != null) {
                        DoctorDetailScreen(doctor = doctor)
                    }
                }
                composable(BottomBarScreen.MoodLog.route) { MoodLogScreen() }
                composable(BottomBarScreen.Chat.route) { ChatScreen() }
            }
        }
    }
}
