package com.example.eudayan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.eudayan.navigation.AppNavigation
import com.example.eudayan.ui.theme.EudayanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EudayanTheme {
                AppNavigation()
            }
        }
    }
}
