package com.adormantsakthi.holup.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ElevatedButton
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.adormantsakthi.holup.R
import com.adormantsakthi.holup.functions.NotificationAlarmReceiver
import com.adormantsakthi.holup.ui.screens.Dialogs.forSettings.FeedbackDialog

@Composable
fun Settings(onNavigate: () -> Unit,
             isAppBarVisible: androidx.compose.runtime.MutableState<Boolean>,
             selectedItemIndex: androidx.compose.runtime.MutableState<Int>,
             isAccessibilityServiceEnabled: Boolean,
             hasUsageStatsPermission: Boolean,
             context: Context,
             canDrawOverlays: Boolean,
             canSendNotifications: Boolean,
             canSendExactAlarms: Boolean
) {
    selectedItemIndex.value = 2
    // Billing stuff
    val billingManager = MainApplication.getInstance().billingManager
    val activity = LocalContext.current as Activity
    val userHasPlus = billingManager.isSubscribed.collectAsState().value
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

    fun getAppVersion(context: Context): String? {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName // Returns versionName from build.gradle
        } catch (e: Exception) {
            "Unknown" // Handle exception if the version info isn't found
        }
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
    val showFeedbackDialog = remember { mutableStateOf(false) }

    val showToast = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Settings",
            style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 15.dp)
                .fillMaxWidth()
        )

        if (!userHasPlus) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .fillMaxWidth(0.85f)
                    .aspectRatio(3 / 1f)
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
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.Black),
                        modifier = Modifier.padding(end = 10.dp),
                    )
                }
            }
        }

        Spacer(Modifier.height(30.dp))

        Row (
            modifier = Modifier
                .padding(
                    bottom = 16.dp,
                    start = (LocalConfiguration.current.screenWidthDp * 0.075).dp
                )
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!userHasPlus) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = "Premium Features",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(30.dp)

                )
            }

            Text(
                text = "Edit HolUp! Popup",
                style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.secondary),
            )
        }

        Card (
            modifier = Modifier
                .fillMaxWidth(0.85f),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
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
                .padding(
                    bottom = 16.dp,
                    start = (LocalConfiguration.current.screenWidthDp * 0.075).dp
                )
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!userHasPlus) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = "Premium Features",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(30.dp)
                )
            }

            Text(
                text = "Anti-Doomscroll",
                style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.secondary),
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
            SettingsSection("Re-Interrupt", "Select apps that you want to prevent doomscrolling in", {
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
                    HolUpPopupPrefs(context).editDelayBtwReinterruptionIndex(ReInterruptionTimeIndex.intValue)
                }
            })
        }

        Spacer(Modifier.height(30.dp))

        Text(
            text = "Setup",
            style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .padding(
                    bottom = 16.dp,
                    start = (LocalConfiguration.current.screenWidthDp * 0.075).dp
                )
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
            SettingsSection("Add Apps", "Select apps that you want to limit", {
                showToast.value = false
                showSetUpAppsToLimitDialog.value = true
                isAppBarVisible.value = false
            })
            SettingsSection("Accessibility Service",
                if (isAccessibilityServiceEnabled) "On" else "Off"
            ) {
                showToast.value = false
                val intent = Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            SettingsSection(
                "App Usage Permission",
                if (hasUsageStatsPermission) "On" else "Off"
            ) {
                showToast.value = false
                val intent = Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

            SettingsSection(
                "Overlay Over Other Apps",
                if (canDrawOverlays) "On" else "Off"
            ) {
                showToast.value = false
                val intent = Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

            val notificationState = remember { mutableStateOf("") }
            if (canSendNotifications && canSendExactAlarms) {
                notificationState.value = "On"
            } else if (canSendNotifications) {
                notificationState.value = "On (Click again to enable more accurate notifications)"
            } else {
                notificationState.value = "Off"
            }

            SettingsSection(
                "Notifications",
                notificationState.value
            ) {
                showToast.value = false
                if (!canSendNotifications) {
                    val NOTIFICATION_PERMISSION_REQUEST_CODE = 100
                    if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                            NOTIFICATION_PERMISSION_REQUEST_CODE
                        )
                    }
                } else {
                    NotificationAlarmReceiver.requestExactAlarmPermission(context)
                }
            }

        }

        Spacer(Modifier.height(30.dp))

        Text(
            text = "Payments",
            style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .padding(
                    bottom = 16.dp,
                    start = (LocalConfiguration.current.screenWidthDp * 0.075).dp
                )
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
                showToast.value = false
                showPurchaseHistoryDialog.value = true
                isAppBarVisible.value = false
            })
            SettingsSection("Cancel Subscription",
                "Cancel Your Ongoing Subscription :("
            ) {
                showToast.value = false
                billingManager.cancelSubscription(activity)
            }
        }

        Spacer(Modifier.height(30.dp))

        Text(
            text = "Feedback",
            style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .padding(
                    bottom = 16.dp,
                    start = (LocalConfiguration.current.screenWidthDp * 0.075).dp
                )
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
            SettingsSection("Send Your Feedback \uD83D\uDE4F", null, {
                showToast.value = false
                showFeedbackDialog.value = true
                isAppBarVisible.value = false
            })
        }

        Spacer(Modifier.height(25.dp))

        Text(
            text = "Donation",
            style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .padding(
                    bottom = 16.dp,
                    start = (LocalConfiguration.current.screenWidthDp * 0.075).dp
                )
                .align(Alignment.Start)
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .fillMaxWidth(0.85f)
                .aspectRatio(1 / 0.5f)
                .clickable {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.buymeacoffee.com/adormantsakthi")
                    )
                    context.startActivity(intent)
                }
        ) {
            Image(
                painter = painterResource(R.drawable.buymeacoffee),
                contentDescription = "BuyMeACoffee Button",
                contentScale = ContentScale.FillBounds
            )
        }

        Spacer(Modifier.height(30.dp))

        Text(
            text = "A Message To You",
            style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .padding(
                    bottom = 16.dp,
                    start = (LocalConfiguration.current.screenWidthDp * 0.075).dp
                )
                .align(Alignment.Start)
        )

        Card (
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(IntrinsicSize.Max),
            colors = CardDefaults.cardColors(
                containerColor = Color(74, 94, 107, 255),
                contentColor = Color.Red,
                disabledContentColor = Color.Red,
                disabledContainerColor = Color.Red
            )
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row (
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Face,
                        contentDescription = "Face",
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier.size(50.dp)
                    )

                    Text(
                        "Our Personal Message",
                        style = MaterialTheme.typography.titleMedium.copy(MaterialTheme.colorScheme.background)
                    )
                }

                Text(
                    "Dear Valued Users,\n\nThis is our first proper application, and we hope you love it ❤\uFE0F. We really hope that this does benefit you in your daily lives, as that would really make our efforts meaningful! If you want to support us further, do consider buying the subscription, so that it motivates us to hopefully make more applications that will prove to serve users such as you. Once again, thank you so much to each and everyone of you to even consider downloading our application! We will try to bring in new features and fix bugs, but please pardon us if it takes some time as we are indie developers trying our level best. Thank you all once again!",
                    style = MaterialTheme.typography.labelSmall.copy(MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                )
            }
        }

        getAppVersion(context)?.let {
            Text(
                "App Version: $it",
                color = Color.Gray,
                fontSize = 14.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(125.dp))
    }

    UpgradeToProDialog(showUpgradeToProDialog, isAppBarVisible, selectedItemIndex)
    EditPopUpTextDialog(showEditPopUpTextDialog, isAppBarVisible, popUpText, selectedItemIndex)
    AntiDoomscrollDialogScreen(showAntiDoomscrollDialog, isAppBarVisible, selectedItemIndex)
    SetupAppsToLimitDialog(showSetUpAppsToLimitDialog, isAppBarVisible, selectedItemIndex)
    PurchaseHistoryDialog(showPurchaseHistoryDialog, isAppBarVisible, billingHistory, selectedItemIndex)
    FeedbackDialog(showFeedbackDialog, isAppBarVisible, selectedItemIndex)

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
                    .padding(top = 70.dp, start = 15.dp, end = 15.dp) // Padding from top for a more centered look
                    .wrapContentWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.85f) // Black background with some transparency
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.Info, // Using error icon as an example
                        contentDescription = "Toast Icon",
                        tint = Color.Yellow, // Yellow icon
                        modifier = Modifier
                            .padding(10.dp)
                            .size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = message,
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(30.dp)
                    ) {
                        Button(
                            onClick = onBuyClick,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
                        ) {
                            Text("Buy", style = MaterialTheme.typography.labelSmall)
                        }

                        Button(
                            onClick = onDismissClick,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Dismiss", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}