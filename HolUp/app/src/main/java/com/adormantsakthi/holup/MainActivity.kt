package com.adormantsakthi.holup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.adormantsakthi.holup.ui.theme.HolUpTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adormantsakthi.holup.ui.components.BottomNavBar
import com.adormantsakthi.holup.ui.screens.Homescreen
import com.adormantsakthi.holup.ui.screens.Settings
import com.adormantsakthi.holup.ui.screens.Statistics

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HolUpTheme {
                // Get the System UI Controller
                val systemUiController = rememberSystemUiController()
                val navController = rememberNavController()

                // Set the status bar and nav bar colours
                systemUiController.setSystemBarsColor(MaterialTheme.colorScheme.background)
                systemUiController.setNavigationBarColor(MaterialTheme.colorScheme.background)

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(), bottom = WindowInsets.statusBars.asPaddingValues().calculateBottomPadding()),
                    bottomBar = { BottomNavBar(navController = navController) },
                    content = { paddingValues ->
                        NavHost(navController, startDestination = "home") {
                            composable("home") {
                                Homescreen (onNavigate = {navController.navigate("home")})
                            }

                            composable("stats") {
                                Statistics (onNavigate = {navController.navigate("stats")})
                            }

                            composable("settings") {
                                Settings (onNavigate = {navController.navigate("settings")})
                            }
                        }
                    }
                )
            }
        }
    }
}