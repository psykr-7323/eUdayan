package com.example.eudayan.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa // For Mood Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList // Added for type hint if needed, though mutableStateListOf handles it
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
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
import com.example.eudayan.community.AddPostScreen
import com.example.eudayan.community.CommunityScreen
import com.example.eudayan.community.Post
import com.example.eudayan.community.PostDetailScreen
import com.example.eudayan.community.RepliesScreen // Assuming this is still used somewhere
import com.example.eudayan.community.Comment
import com.example.eudayan.home.HomeScreen
import com.example.eudayan.mood.MoodLogScreen
import com.example.eudayan.settings.SettingsScreen
import com.example.eudayan.ui.theme.SurfacePureWhite
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
    object Settings : BottomBarScreen("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun AppDrawer(
    onSignOut: () -> Unit,
    onMoodLog: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column {
        // User Profile Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.user_image),
                contentDescription = "User Profile Image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "User Name",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "user.name@example.com",
                style = MaterialTheme.typography.bodySmall
            )
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Spa, contentDescription = "Mood Log") },
            label = { Text("Mood Log") },
            selected = false,
            onClick = onMoodLog,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = false,
            onClick = onSettingsClick,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        Spacer(modifier = Modifier.weight(1f))
        NavigationDrawerItem(
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sign Out") },
            label = { Text("Sign Out") },
            selected = false,
            onClick = onSignOut,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
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
        Doctor(1, "Dr. Aafreen Khan", 4.9, R.drawable.female_doc1, "Dr. Aafreen Khan is a compassionate and dedicated professional with over 10 years of experience in mental health and wellness. She specializes in cognitive-behavioral therapy and mindfulness practices."),
        Doctor(2, "Dr. Sameer Wani", 4.8, R.drawable.male_doc1, "Dr. Sameer Wani is a clinical psychologist with a focus on adolescent and young adult mental health. He is known for his approachable and empathetic nature, making it easy for patients to open up and share their concerns."),
        Doctor(3, "Dr. Nida Shah", 4.5, R.drawable.female_doc2, "Dr. Nida Shah is a seasoned psychiatrist with a passion for helping individuals achieve mental and emotional balance. She combines medication management with holistic therapies to provide comprehensive care."),
        Doctor(4, "Dr. Imran Bhat", 4.2, R.drawable.male_doc2, "Dr. Imran Bhat is a renowned expert in addiction and recovery. He has helped countless individuals overcome substance abuse and build a foundation for a healthier, more fulfilling life.")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showNavigationPill = currentDestination?.route in screens.map { it.route }
    val showTopBar = currentDestination?.route != BottomBarScreen.Chat.route && currentDestination?.route != BottomBarScreen.Settings.route

    val posts: SnapshotStateList<Post> = remember {
        mutableStateListOf(
            Post(
                author = "Anonymous",
                authorImage = R.drawable.user1,
                time = "2h",
                views = 15,
                content = "I'm feeling so overwhelmed with my studies lately. It feels like no matter how much I study, it's never enough. How do you guys deal with academic pressure?",
                likes = 5,
                isLiked = false,
                comments = mutableStateListOf(
                    Comment(
                        id = "post1_comment1",
                        author = "Anonymous",
                        authorImage = R.drawable.user2,
                        comment = "I feel you! It's tough, but I try to break things down into smaller tasks.",
                        likes = 2,
                        isMod = false,
                        isLiked = false
                    ),
                    Comment(
                        id = "post1_comment2",
                        author = "Mod",
                        authorImage = R.drawable.user_image,
                        comment = "It sounds like you're going through a lot. Remember to take breaks and be kind to yourself. We have some resources on managing stress in the app.",
                        likes = 0,
                        isMod = true,
                        isLiked = false
                    )
                )
            ),
            Post(
                author = "Anonymous",
                authorImage = R.drawable.user3,
                time = "5h",
                views = 25,
                content = "My girlfriend and I broke up last week, and I can't stop thinking about her. It hurts so much. Any advice on how to move on?",
                likes = 10,
                isLiked = false,
                comments = mutableStateListOf(
                    Comment(
                        id = "post2_comment1",
                        author = "Anonymous",
                        authorImage = R.drawable.user4,
                        comment = "Time heals all wounds. Focus on yourself for a bit.",
                        likes = 3,
                        isMod = false,
                        isLiked = false
                    ),
                    Comment(
                        id = "post2_comment2",
                        author = "Mod",
                        authorImage = R.drawable.user_image,
                        comment = "Breakups are tough. It's okay to feel sad. Talking to a friend or a professional can really help.",
                        likes = 0,
                        isMod = true,
                        isLiked = false
                    )
                )
            ),
            Post(
                author = "Anonymous",
                authorImage = R.drawable.user5,
                time = "1d",
                views = 50,
                content = "I'm having trouble making friends at my new school. Everyone seems to have their own groups already. What should I do?",
                likes = 12,
                isLiked = false,
                comments = mutableStateListOf(
                    Comment(
                        id = "post3_comment1",
                        author = "Anonymous",
                        authorImage = R.drawable.user6,
                        comment = "Join a club or a sports team! It's a great way to meet people.",
                        likes = 5,
                        isMod = false,
                        isLiked = false
                    ),
                    Comment(
                        id = "post3_comment2",
                        author = "Mod",
                        authorImage = R.drawable.user_image,
                        comment = "Making new friends can be intimidating. Remember that you are not alone in this. We have a great article on social skills in our resource center.",
                        likes = 0,
                        isMod = true,
                        isLiked = false
                    )
                )
            ),
            Post(
                author = "Anonymous",
                authorImage = R.drawable.user7,
                time = "2d",
                views = 30,
                content = "I'm constantly comparing myself to others on social media. It's making me feel so insecure. How do I stop?",
                likes = 8,
                isLiked = false,
                comments = mutableStateListOf(
                    Comment(
                        id = "post4_comment1",
                        author = "Anonymous",
                        authorImage = R.drawable.user8,
                        comment = "I deleted most of my social media apps. It helped a lot.",
                        likes = 4,
                        isMod = false,
                        isLiked = false
                    ),
                    Comment(
                        id = "post4_comment2",
                        author = "Mod",
                        authorImage = R.drawable.user_image,
                        comment = "Social media can be a highlight reel of people's lives. It's important to remember that it's not always reality. Our article on digital wellness might be helpful.",
                        likes = 0,
                        isMod = true,
                        isLiked = false
                    )
                )
            )
        )
    }

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
                    },
                    onSettingsClick = { 
                        scope.launch {
                            drawerState.close()
                            navController.navigate(BottomBarScreen.Settings.route)
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background, // Set neutral background
            topBar = {
                if (showTopBar) {
                    val isTopLevelDestination = currentDestination?.route in screens.map { it.route }
                    TopAppBar(
                        title = { Text("eUdyaan") }, 
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        navigationIcon = {
                            if (isTopLevelDestination) {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onPrimary)
                                }
                            } else {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onPrimary)
                                }
                            }
                        }
                    )
                }
            },
            floatingActionButton = {
                if (showNavigationPill) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp) // Width for the main pill
                            .padding(bottom = 4.dp)  // Lifts the main pill by 4.dp
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(percent = 50),
                            color = SurfacePureWhite,
                            shadowElevation = 12.dp // Increased shadow elevation
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp), // Internal padding for icons in the pill
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                screens.forEach { screen ->
                                    screen.icon?.let {
                                        val isSelected = currentDestination?.hierarchy?.any { hir -> hir.route == screen.route } == true
                                        val iconColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                        IconButton(
                                            onClick = {
                                                if (!isSelected) {
                                                    navController.navigate(screen.route) {
                                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                }
                                            }
                                        ) {
                                            Icon(it, contentDescription = screen.title, tint = iconColor, modifier = Modifier.size(24.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center, // Center the main navigation pill
        ) { innerPadding ->
            // NavHost content area now extends to the bottom of the screen (behind the FAB)
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) { 
                NavHost(
                    navController = navController,
                    startDestination = BottomBarScreen.Home.route
                ) {
                    composable(BottomBarScreen.Home.route) { HomeScreen(showSignupSuccess = showSignupSuccess) }
                    composable(BottomBarScreen.Community.route) { CommunityScreen(navController, posts) }
                    composable(BottomBarScreen.Booking.route) { BookingScreen(navController, doctors) }
                    composable("${BottomBarScreen.PostDetail.route}/{postIndex}") { backStackEntry ->
                        val postIndex = backStackEntry.arguments?.getString("postIndex")?.toInt() ?: 0
                        if (postIndex >= 0 && postIndex < posts.size) { // Check bounds
                            PostDetailScreen(navController, posts, postIndex)
                        } else {
                            // Handle invalid postIndex, e.g., navigate back or show an error
                            Text("Error: Post not found") 
                        }
                    }
                    composable("${BottomBarScreen.Replies.route}/{commentLikes}") { backStackEntry ->
                        val commentLikes = backStackEntry.arguments?.getString("commentLikes")?.toInt() ?: 0
                        RepliesScreen(navController, commentLikes)
                    }
                    composable(BottomBarScreen.AddPost.route) {
                        AddPostScreen(navController) { text ->
                            posts.add(0, Post(
                                author = "Me", 
                                authorImage = R.drawable.user_image, 
                                time = "now",
                                views = 0,
                                content = text,
                                imageUrl = null,
                                likes = 0,            
                                isLiked = false,      
                                comments = mutableStateListOf()
                            ))
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
                    composable(BottomBarScreen.Chat.route) { ChatScreen(navController = navController) }
                    composable(BottomBarScreen.Settings.route) { SettingsScreen(navController = navController) }
                }

                // Contextual FAB (Chat/Add Post)
                val currentRoute = currentDestination?.route
                if (showNavigationPill && (currentRoute == BottomBarScreen.Home.route || currentRoute == BottomBarScreen.Community.route)) {
                    FloatingActionButton(
                        onClick = {
                            if (currentRoute == BottomBarScreen.Home.route) {
                                navController.navigate(BottomBarScreen.Chat.route)
                            } else if (currentRoute == BottomBarScreen.Community.route) {
                                navController.navigate(BottomBarScreen.AddPost.route)
                            }
                        },
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 24.dp, bottom = 82.dp) 
                    ) {
                        if (currentRoute == BottomBarScreen.Home.route) {
                            Image(
                                painter = painterResource(id = R.drawable.man_sathi),
                                contentDescription = "Chat",
                                modifier = Modifier.size(60.dp) 
                            )
                        } else if (currentRoute == BottomBarScreen.Community.route) {
                            Icon(Icons.Default.Add, contentDescription = "Add Post", modifier = Modifier.size(32.dp))
                        }
                    }
                }
            }
        }
    }
}
