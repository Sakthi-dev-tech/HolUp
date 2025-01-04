package com.adormantsakthi.holup

import android.accessibilityservice.AccessibilityService
import android.app.Activity
import android.app.AppOpsManager
import android.content.ComponentName
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.getSystemService
import com.adormantsakthi.holup.ui.theme.HolUpTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adormantsakthi.holup.ui.components.BottomNavBar
import com.adormantsakthi.holup.ui.screens.Homescreen
import com.adormantsakthi.holup.ui.screens.Settings
import com.adormantsakthi.holup.ui.screens.Statistics
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HolUpTheme {
                // Get the System UI Controller
                val systemUiController = rememberSystemUiController()
                val navController = rememberNavController()
                val selectedItemIndex = remember { mutableIntStateOf(1) }

                // Set the status bar and nav bar colours
                systemUiController.setSystemBarsColor(MaterialTheme.colorScheme.background)
                systemUiController.setNavigationBarColor(MaterialTheme.colorScheme.background)

                // control visibility of bottom app bar
                val isAppBarVisible = remember { mutableStateOf(true) }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(), bottom = WindowInsets.statusBars.asPaddingValues().calculateBottomPadding()),
                    bottomBar = {
                        if (isAppBarVisible.value) {
                            BottomNavBar(navController = navController, selectedItemIndex = selectedItemIndex)
                        }
                                },
                    content = { paddingValues ->
                        NavHost(navController, modifier = Modifier.fillMaxSize(), startDestination = "home") {
                            composable("home") {
                                Homescreen(onNavigate = {navController.navigate("home")}, isAppBarVisible)
                            }

                            composable("stats") {
                                Statistics(onNavigate = {navController.navigate("stats")})
                            }

                            composable("settings") {
                                Settings(onNavigate = {navController.navigate("settings")}, isAppBarVisible, selectedItemIndex)
                            }
                        }
                    }
                )
            }
        }
    }
}