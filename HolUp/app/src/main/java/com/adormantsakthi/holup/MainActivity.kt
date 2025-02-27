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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.adormantsakthi.holup.ui.theme.HolUpTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adormantsakthi.holup.functions.NotificationAlarmReceiver
import com.adormantsakthi.holup.functions.statistics.NumOfTimesLimitedAppsAccessedStorage
import com.adormantsakthi.holup.ui.components.BottomNavBar
import com.adormantsakthi.holup.ui.screens.AppSession
import com.adormantsakthi.holup.ui.screens.Homescreen
import com.adormantsakthi.holup.ui.screens.Settings
import com.adormantsakthi.holup.ui.screens.Statistics
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val hasUsageStatsPerm =  mutableStateOf(false)
    private val accessibilityServiceOn = mutableStateOf(false)
    private val drawOverlayPermission = mutableStateOf(false)
    private val canSendNotifications = mutableStateOf(false)
    private val canSendExactAlarms = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{true}

        // Get rid of the splash screen after a second...maybe can run some async functions here
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            splashScreen.setKeepOnScreenCondition{false}
        }

        NumOfTimesLimitedAppsAccessedStorage.refreshCounter(this)

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
                                    onNavigate = {navController.navigate("home")},
                                    isAppBarVisible,
                                    selectedItemIndex,
                                    accessibilityServiceOn.value && hasUsageStatsPerm.value && drawOverlayPermission.value,
                                )
                            }

                            composable("stats") {
                                Statistics(onNavigate = {navController.navigate("stats")}, selectedItemIndex)
                            }

                            composable("settings") {
                                Settings(onNavigate = {navController.navigate("settings")},
                                    isAppBarVisible,
                                    selectedItemIndex,
                                    accessibilityServiceOn.value,
                                    hasUsageStatsPerm.value,
                                    LocalContext.current,
                                    drawOverlayPermission.value,
                                    canSendNotifications.value,
                                    canSendExactAlarms.value
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
        accessibilityServiceOn.value = isAccessibilityServiceEnabled(this, MyAccessibilityService::class.java)
        drawOverlayPermission.value = Settings.canDrawOverlays(this)
        canSendNotifications.value = checkNotificationPermission(this)
        canSendExactAlarms.value = checkExactAlarmPermission(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)

        if (checkNotificationPermission(this)) {
            canSendExactAlarms.value = true
            NotificationAlarmReceiver.scheduleDaily(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Reset the session when app goes to background
        AppSession.reset()
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

    fun checkExactAlarmPermission(context: Context): Boolean {
        return NotificationAlarmReceiver.canScheduleExactAlarms(context)
    }
}