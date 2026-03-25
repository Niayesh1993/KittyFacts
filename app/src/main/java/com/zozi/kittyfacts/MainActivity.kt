package com.zozi.kittyfacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zozi.kittyfacts.presentation.kittyfact.screen.KittyFactScreen
import com.zozi.kittyfacts.presentation.theme.KittyFactsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KittyFactsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KittyFactScreen(
                        Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Keeping preview simple (no Hilt in previews)
@Preview(showBackground = true)
@Composable
fun MainPreview() {
    KittyFactsTheme {
        KittyFactScreen()
    }
}