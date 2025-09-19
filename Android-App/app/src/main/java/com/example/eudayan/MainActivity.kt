package com.example.eudayan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.eudayan.navigation.AppNavigation
import com.example.eudayan.ui.theme.EudayanTheme
import dagger.hilt.android.AndroidEntryPoint
import com.example.eudayan.IntroVideoActivity // Added explicit import

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get the potential route from the Intent that started this Activity
        val navigateToRoute = intent.getStringExtra(IntroVideoActivity.EXTRA_NAVIGATE_TO_ROUTE)

        setContent {
            EudayanTheme {
                // Pass the initial route to AppNavigation
                AppNavigation(initialRoute = navigateToRoute)
            }
        }
    }
}
