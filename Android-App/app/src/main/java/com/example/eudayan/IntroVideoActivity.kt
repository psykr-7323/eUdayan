package com.example.eudayan

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.eudayan.ui.theme.EudayanTheme

class IntroVideoActivity : ComponentActivity() {

    companion object {
        const val EXTRA_NAVIGATE_TO_ROUTE = "NAVIGATE_TO_ROUTE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            EudayanTheme {
                IntroVideoPlayer()
            }
        }
    }

    private fun navigateToMain(route: String) {
        val intent = Intent(this@IntroVideoActivity, MainActivity::class.java).apply {
            putExtra(EXTRA_NAVIGATE_TO_ROUTE, route)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        overridePendingTransition(0, 0) // Added this line
        finish()
    }

    @Composable
    fun IntroVideoPlayer() {
        var videoAlpha by remember { mutableStateOf(0f) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { context ->
                    VideoView(context).apply {
                        val videoUri = Uri.parse("android.resource://${packageName}/raw/intro_video")
                        setVideoURI(videoUri)
                        setOnPreparedListener { mp ->
                            mp.start()
                        }
                        setOnInfoListener { mp, what, extra ->
                            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                videoAlpha = 1f
                                true
                            } else {
                                false
                            }
                        }
                        setOnCompletionListener {
                            navigateToMain(route = "login_selection")
                        }
                        setOnErrorListener { _, _, _ ->
                            videoAlpha = 1f
                            navigateToMain(route = "login_selection")
                            true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(alpha = videoAlpha)
            )
        }
    }
}
