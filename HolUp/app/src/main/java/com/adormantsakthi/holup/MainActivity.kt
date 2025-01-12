package com.adormantsakthi.holup

import android.Manifest
import android.accessibilityservice.AccessibilityService
import android.app.AppOpsManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.adormantsakthi.holup.ui.theme.HolUpTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adormantsakthi.holup.functions.NotificationWorker
import com.adormantsakthi.holup.ui.components.BottomNavBar
import com.adormantsakthi.holup.ui.screens.Homescreen
import com.adormantsakthi.holup.ui.screens.Settings
import com.adormantsakthi.holup.ui.screens.Statistics
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val hasUsageStatsPerm =  mutableStateOf(false)
    private val accessibililityServiceOn = mutableStateOf(false)
    private val drawOverlayPermission = mutableStateOf(false)
    private val canSendNotifications = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HolUpTheme {
                val context = this

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
                                Homescreen(
                                    onNavigate = {navController.navigate("home")}, isAppBarVisible)
                            }

                            composable("stats") {
                                Statistics(onNavigate = {navController.navigate("stats")})
                            }

                            composable("settings") {
                                Settings(onNavigate = {navController.navigate("settings")},
                                    isAppBarVisible,
                                    selectedItemIndex,
                                    accessibililityServiceOn.value,
                                    hasUsageStatsPerm.value,
                                    LocalContext.current,
                                    drawOverlayPermission.value,
                                    canSendNotifications.value
                                )
                            }
                        }
                    }
                )
            }
        }
    }

    // When the app gains focus run this function
    override fun onResume() {
        super.onResume()

        // Update permission states
        hasUsageStatsPerm.value = hasUsageStatsPermission()
        accessibililityServiceOn.value = isAccessibilityServiceEnabled(this, MyAccessibilityService::class.java)
        drawOverlayPermission.value = Settings.canDrawOverlays(this)
        canSendNotifications.value = checkNotificationPermission(this)
    }

    // Helper functions to check if permissions are granted

    // Usage Stats Permission
    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    // Accessibility Service Permission
    private fun isAccessibilityServiceEnabled(context: Context, service: Class<out AccessibilityService>): Boolean {
        val componentName = ComponentName(context, service)
        val enabledServices =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        val accessibilityEnabled =
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 0)

        return accessibilityEnabled == 1 && enabledServices?.contains(componentName.flattenToString()) == true
    }

    // Notifications Permission
    fun checkNotificationPermission(context: Context): Boolean {
        // For Android 13 and above (API level 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }

        // For Android 12 and below, no runtime permission needed
        return true
    }

    fun sendTestNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel (required for Android 8.0 and above)
        val channel = NotificationChannel(
            "test_channel",
            "Test Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Channel for testing notifications"
        }
        notificationManager.createNotificationChannel(channel)

        // Build the notification
        val notification = NotificationCompat.Builder(context, "test_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Use your app icon here
            .setContentTitle("Test Notification")
            .setContentText("This is a test notification!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        // Show the notification
        notificationManager.notify(1, notification)
    }
}