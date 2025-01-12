package com.adormantsakthi.holup.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.adormantsakthi.holup.storage.HolUpPopupPrefs
import com.adormantsakthi.holup.storage.LimitedAppsStorage
import com.adormantsakthi.holup.storage.ReInterruptionStorage
import com.adormantsakthi.holup.ui.Todo.MainApplication
import com.adormantsakthi.holup.ui.screens.Dialogs.forSettings.AntiDoomscrollDialogScreen
import com.adormantsakthi.holup.ui.screens.Dialogs.forSettings.EditPopUpTextDialog
import com.adormantsakthi.holup.ui.screens.Dialogs.forSettings.SetupAppsToLimitDialog
import com.adormantsakthi.holup.ui.screens.Dialogs.forSettings.UpgradeToProDialog
import com.adormantsakthi.holup.ui.components.forSettings.SettingsSection
import com.adormantsakthi.holup.ui.screens.Dialogs.forSettings.PurchaseHistoryDialog

@Composable
fun Settings(onNavigate: () -> Unit,
             isAppBarVisible: androidx.compose.runtime.MutableState<Boolean>,
             selectedItemIndex: androidx.compose.runtime.MutableState<Int>,
             isAccessibilityServiceEnabled: Boolean,
             hasUsageStatsPermission: Boolean,
             context: Context,
             canDrawOverlays: Boolean,
) {
    // Billing stuff
    val billingManager = MainApplication.getInstance().billingManager
    val activity = LocalContext.current as Activity
    val userHasPlus = billingManager.isSubscribed.collectAsState(initial = false).value
    val billingHistory = billingManager.getPurchaseHistory()

    Log.d("Settings", "User subbed: $userHasPlus")

    // Run following functions depending on user's subscription status

    if (!userHasPlus) {
        /** if user is not subscribed anymore
         * set back everything to the default
         * remove all the reinterrupt apps
         * prevent unsubbed user from editing any of the premium features
         */
        HolUpPopupPrefs(context).editInterruptionDuration(1)
        HolUpPopupPrefs(context).editDelayBtwAppSwitchIndex(0)
        HolUpPopupPrefs(context).editDelayBtwReinterruptionIndex(4)
        HolUpPopupPrefs(context).editInterruptionText("HolUp! You have these remaining tasks!")
        ReInterruptionStorage(context).clearData()
        LimitedAppsStorage(context).clearPackagesKeepTwo()
    }

    // For Pop Up Duration
    val PopUpDuration = listOf("Short", "Medium", "Long")
    val PopUpDurationIndex = remember { mutableIntStateOf(HolUpPopupPrefs(context).getInterruptionDurationIndex()) }

    // For Delay Between App Switch
    val DelayBetweenAppSwitch = listOf("1 minute", "2 minutes", "5 minutes", "10 minutes")
    val DelayBetweenAppSwitchIndex = remember { mutableIntStateOf(HolUpPopupPrefs(context).getDelayBtwAppSwitchIndex()) }

    // For re-interruption
    val ReInterruptionTime = listOf("1 minute", "2 minutes", "5 minutes", "10 minutes", "N/A")
    val ReInterruptionTimeIndex = remember { mutableIntStateOf(HolUpPopupPrefs(context).getDelayBtwReinterruptionIndex()) }

    // variables that can change
    var popUpText = remember { mutableStateOf(HolUpPopupPrefs(context).getInterruptionText()) }

    // show Dialog Screens
    val showUpgradeToProDialog = remember { mutableStateOf(false) }
    val showEditPopUpTextDialog = remember { mutableStateOf(false) }
    val showAntiDoomscrollDialog = remember { mutableStateOf(false) }
    val showSetUpAppsToLimitDialog = remember { mutableStateOf(false) }
    val showPurchaseHistoryDialog = remember { mutableStateOf(false) }

    val showToast = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Settings",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 15.dp)
                .fillMaxWidth()
        )

        if (!userHasPlus) {
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
        }

        Spacer(Modifier.height(30.dp))

        Row (
            modifier = Modifier
                .padding(bottom = 16.dp, start = (LocalConfiguration.current.screenWidthDp * 0.075).dp)
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!userHasPlus) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    tint = Color.White,
                    contentDescription = "Premium Features",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(30.dp)

                )
            }

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
                if (!userHasPlus) {
                    showToast.value = true
                } else {
                    showEditPopUpTextDialog.value = true
                    isAppBarVisible.value = false
                }
            })
            SettingsSection("Interruption Duration", PopUpDuration[PopUpDurationIndex.intValue], {
                if (!userHasPlus){
                    showToast.value = true
                } else {
                    PopUpDurationIndex.intValue = (PopUpDurationIndex.intValue + 1) % 3
                    HolUpPopupPrefs(context).editInterruptionDuration(PopUpDurationIndex.intValue)
                }
            })
            SettingsSection("Delay Between App Switch", DelayBetweenAppSwitch[DelayBetweenAppSwitchIndex.intValue], {
                if (!userHasPlus) {
                    showToast.value = true
                } else {
                    DelayBetweenAppSwitchIndex.intValue = (DelayBetweenAppSwitchIndex.intValue + 1) % 4
                    HolUpPopupPrefs(context).editDelayBtwAppSwitchIndex(DelayBetweenAppSwitchIndex.intValue)
                }

            })
        }

        Spacer(Modifier.height(30.dp))

        Row (
            modifier = Modifier
                .padding(bottom = 16.dp, start = (LocalConfiguration.current.screenWidthDp * 0.075).dp)
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!userHasPlus) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    tint = Color.White,
                    contentDescription = "Premium Features",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(30.dp)
                )
            }

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
            SettingsSection("Re-Interrupt", null, {
                if (!userHasPlus) {
                    showToast.value = true
                } else {
                    showAntiDoomscrollDialog.value = true
                    isAppBarVisible.value = false
                }
            })

            SettingsSection("Re-Interrupt Timeout", ReInterruptionTime[ReInterruptionTimeIndex.intValue], {
                if (!userHasPlus){
                    showToast.value = true
                } else {
                    if (ReInterruptionTimeIndex.intValue == 4) {
                        ReInterruptionTimeIndex.intValue = 0
                    } else {
                        ReInterruptionTimeIndex.intValue = (ReInterruptionTimeIndex.intValue + 1) % 4
                    }
                    HolUpPopupPrefs(context).editDelayBtwReinterruptionIndex((ReInterruptionTimeIndex.intValue + 1) % 4)
                }
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
                if (isAccessibilityServiceEnabled) "On" else "Off"
            ) {
                val intent = Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            SettingsSection(
                "App Usage Permission",
                if (hasUsageStatsPermission) "On" else "Off"
            ) {
                val intent = Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

            SettingsSection(
                "Overlay Over Other Apps",
                if (canDrawOverlays) "On" else "Off"
            ) {
                val intent = Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

        }

        Spacer(Modifier.height(30.dp))

        Text(
            text = "Payments",
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
            SettingsSection("Payment History", null, {
                showPurchaseHistoryDialog.value = true
                isAppBarVisible.value = false
            })
            SettingsSection("Cancel Subscription",
                "Cancel Your Ongoing Subscription :("
            ) {
                billingManager.cancelSubscription(activity)
            }
        }

        Spacer(modifier = Modifier.height(125.dp))
    }

    UpgradeToProDialog(showUpgradeToProDialog, isAppBarVisible, selectedItemIndex)
    EditPopUpTextDialog(showEditPopUpTextDialog, isAppBarVisible, popUpText, selectedItemIndex)
    AntiDoomscrollDialogScreen(showAntiDoomscrollDialog, isAppBarVisible, selectedItemIndex)
    SetupAppsToLimitDialog(showSetUpAppsToLimitDialog, isAppBarVisible, selectedItemIndex)
    PurchaseHistoryDialog(showPurchaseHistoryDialog, isAppBarVisible, billingHistory, selectedItemIndex)

    SubscriptionToast("This is a Plus Feature!", showToast.value,
        {
            showToast.value = false
            showUpgradeToProDialog.value = true
            isAppBarVisible.value = false
        },
        {showToast.value = false }
    )
}

@Composable
fun SubscriptionToast(
    message: String,
    isVisible: Boolean,
    onBuyClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = 600)) + slideInVertically(
            initialOffsetY = { -100 } // Toast starts from above
        ),
        exit = fadeOut(animationSpec = tween(durationMillis = 600)) + slideOutVertically(
            targetOffsetY = { -100 } // Toast slides back up
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.9f)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                modifier = Modifier
                    .padding(top = 48.dp) // Padding from top for a more centered look
                    .wrapContentWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.85f) // Black background with some transparency
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.Info, // Using error icon as an example
                        contentDescription = "Toast Icon",
                        tint = Color.Yellow, // Yellow icon
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = message,
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onBuyClick,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
                        ) {
                            Text("Buy", color = Color.Black)
                        }
                        Button(
                            onClick = onDismissClick,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Dismiss", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}