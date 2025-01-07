package com.adormantsakthi.holup.ui.screens

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.adormantsakthi.holup.ui.components.Dialogs.forSettings.AntiDoomscrollDialogScreen
import com.adormantsakthi.holup.ui.components.Dialogs.forSettings.EditPopUpTextDialog
import com.adormantsakthi.holup.ui.components.Dialogs.forSettings.SetupAppsToLimitDialog
import com.adormantsakthi.holup.ui.components.Dialogs.forSettings.UpgradeToProDialog
import com.adormantsakthi.holup.ui.components.forSettings.SettingsSection

@Composable
fun Settings(onNavigate: () -> Unit,
             isAppBarVisible: androidx.compose.runtime.MutableState<Boolean>,
             selectedItemIndex: androidx.compose.runtime.MutableState<Int>,
             isAccessibilityServiceEnabled: Boolean,
             hasUsageStatsPermission: Boolean,
             context: Context,
             canDrawOverlays: Boolean,
             listOfApps: List<Triple<String, Drawable, ApplicationInfo>>
) {

    // For Pop Up Duration
    val PopUpDuration = listOf("Short", "Medium", "Long")
    val PopUpDurationIndex = remember { mutableIntStateOf(1) }

    // For Delay Between App Switch
    val DelayBetweenAppSwitch = listOf("1 minute", "2 minutes", "5 minutes", "10 minutes")
    val DelayBetweenAppSwitchIndex = remember { mutableIntStateOf(0) }

    // variables that can change
    var popUpText = remember { mutableStateOf("HolUp! You have these remaining tasks!") }

    // show Dialog Screens
    val showUpgradeToProDialog = remember { mutableStateOf(false) }
    val showEditPopUpTextDialog = remember { mutableStateOf(false) }
    val showAntiDoomscrollDialog = remember { mutableStateOf(false) }
    val showSetUpAppsToLimitDialog = remember { mutableStateOf(false) }

    if (listOfApps.size > 0) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = ScrollState(0)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Settings",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(vertical = 20.dp, horizontal = 15.dp)
                    .fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .fillMaxWidth(0.85f)
                    .aspectRatio(3/1f)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .clickable {
                        showUpgradeToProDialog.value = true
                        isAppBarVisible.value = false
                    }
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "Star",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(start = 20.dp),
                        tint = Color.Black
                    )

                    Text(
                        "Upgrade to HolUp! Plus",
                        style = MaterialTheme.typography.titleMedium.copy(Color.Black),
                        modifier = Modifier.padding(end = 10.dp),
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            Row (
                modifier = Modifier
                    .padding(bottom = 16.dp, start = (LocalConfiguration.current.screenWidthDp * 0.075).dp)
                    .align(Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    tint = Color.White,
                    contentDescription = "Premium Features",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(30.dp)

                )

                Text(
                    text = "Edit HolUp! Popup",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                )
            }

            Card (
                modifier = Modifier
                    .fillMaxWidth(0.85f),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.Red,
                    disabledContentColor = Color.Red,
                    disabledContainerColor = Color.Red
                )
            ) {
                SettingsSection("Interruption Text", popUpText.value, {
                    showEditPopUpTextDialog.value = true
                    isAppBarVisible.value = false
                })
                SettingsSection("Interruption Duration", PopUpDuration[PopUpDurationIndex.intValue], {
                    PopUpDurationIndex.intValue = (PopUpDurationIndex.intValue + 1) % 3
                })
                SettingsSection("Delay Between App Switch", DelayBetweenAppSwitch[DelayBetweenAppSwitchIndex.intValue], {
                    DelayBetweenAppSwitchIndex.intValue = (DelayBetweenAppSwitchIndex.intValue + 1) % 4
                })
            }

            Spacer(Modifier.height(30.dp))

            Row (
                modifier = Modifier
                    .padding(bottom = 16.dp, start = (LocalConfiguration.current.screenWidthDp * 0.075).dp)
                    .align(Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    tint = Color.White,
                    contentDescription = "Premium Features",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(30.dp)

                )

                Text(
                    text = "Anti-Doomscroll",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                )
            }

            Card (
                modifier = Modifier
                    .fillMaxWidth(0.85f),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.Red,
                    disabledContentColor = Color.Red,
                    disabledContainerColor = Color.Red
                )
            ) {
                SettingsSection("Re-Popup", null, {
                    showAntiDoomscrollDialog.value = true
                    isAppBarVisible.value = false
                })
            }

            Spacer(Modifier.height(30.dp))

            Text(
                text = "Setup",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 16.dp, start = (LocalConfiguration.current.screenWidthDp * 0.075).dp)
                    .align(Alignment.Start)
            )

            Card (
                modifier = Modifier
                    .fillMaxWidth(0.85f),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.Red,
                    disabledContentColor = Color.Red,
                    disabledContainerColor = Color.Red
                )
            ) {
                SettingsSection("Add Apps", "Select Apps that you want to limit", {
                    showSetUpAppsToLimitDialog.value = true
                    isAppBarVisible.value = false
                })
                SettingsSection("Accessibility Service",
                    if (isAccessibilityServiceEnabled) "On" else "Off",
                    {
                        val intent = Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                )
                SettingsSection(
                    "App Usage Permission",
                    if (hasUsageStatsPermission) "On" else "Off",
                    {
                        val intent = Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                )

                SettingsSection(
                    "Overlay Over Other Apps",
                    if (canDrawOverlays) "On" else "Off",
                    {
                        val intent = Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                )

            }

            Spacer(Modifier.height(150.dp))
        }
    } else {

    }


    UpgradeToProDialog(showUpgradeToProDialog, isAppBarVisible, selectedItemIndex)
    EditPopUpTextDialog(showEditPopUpTextDialog, isAppBarVisible, popUpText, selectedItemIndex)
    AntiDoomscrollDialogScreen(showAntiDoomscrollDialog, isAppBarVisible, selectedItemIndex)
    SetupAppsToLimitDialog(showSetUpAppsToLimitDialog, isAppBarVisible, selectedItemIndex, listOfApps)
    }