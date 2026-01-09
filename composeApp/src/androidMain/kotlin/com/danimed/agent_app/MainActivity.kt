package com.danimed.agent_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.danimed.agent_app.shared.components.VideoPreloader
import com.danimed.agent_app.shared.utils.CredentialsManagerProvider
import com.danimed.agent_app.shared.utils.RecentSearchesManagerProvider
import com.danimed.agent_app.shared.utils.TokenManagerProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        TokenManagerProvider.init(this)
        RecentSearchesManagerProvider.init(this)
        CredentialsManagerProvider.init(this)

        setContent {
            // Precargar el video de no internet
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                GlobalScope.launch {
                    VideoPreloader.preloadVideo(context, "no_connection")
                }
            }
            
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}