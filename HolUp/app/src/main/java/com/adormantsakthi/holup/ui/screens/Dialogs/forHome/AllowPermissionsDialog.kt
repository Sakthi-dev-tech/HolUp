package com.adormantsakthi.holup.ui.screens.Dialogs.forHome

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adormantsakthi.holup.ui.screens.OnBoardingScreens.PermissionAnimation

@Composable
fun AllowPermissionsDialog(
    showDialog: MutableState<Boolean>,
    isAppBarVisible: MutableState<Boolean>,
    showAddAppsDialog: MutableState<Boolean>,
) {
    if (showDialog.value) {
        isAppBarVisible.value = false
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(40.dp)
                        .align(Alignment.End)
                        .clickable {
                            showDialog.value = false
                            if (!showAddAppsDialog.value) {
                                isAppBarVisible.value = true
                            }
                        }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        "Hey there! We notice that you do not have all permissions enabled!",
                        style = MaterialTheme.typography.labelMedium.copy(
                            MaterialTheme.colorScheme.primary,
                            fontSize = 36.sp
                        ),
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )
                }


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.6f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        PermissionAnimation()
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp)
                        .weight(1.4f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        "Please do enable them in the Settings page for proper use!",
                        style = MaterialTheme.typography.labelMedium.copy(
                            MaterialTheme.colorScheme.primary,
                            fontSize = 32.sp,
                            textAlign = TextAlign.End
                        ),
                    )
                }
            }
        }
    }
}