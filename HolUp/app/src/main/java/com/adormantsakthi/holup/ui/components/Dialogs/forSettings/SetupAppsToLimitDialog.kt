package com.adormantsakthi.holup.ui.components.Dialogs.forSettings

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SetupAppsToLimitDialog (
    showDialog: MutableState<Boolean>,
    isAppBarVisible: MutableState<Boolean>,
    selectedItemIndex: MutableState<Int>
) {
    if (showDialog.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = ScrollState(0)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(40.dp)
                        .clickable {
                            showDialog.value = false
                            isAppBarVisible.value = true
                            selectedItemIndex.value = 2
                        }
                        .align(Alignment.Start)
                )

                Text(
                    "Apps To Limit",
                    style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)
                )

                Box(
                    modifier = Modifier
                        .padding(top = 30.dp, bottom = 30.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .fillMaxWidth(0.95f)
                        .aspectRatio(1/1.2f)
                        .background(Color.DarkGray)
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Select Apps",
                            style = MaterialTheme.typography.labelMedium.copy(Color.White),
                            modifier = Modifier
                                .padding(20.dp)
                                .align(Alignment.Start)
                        )

                        HorizontalDivider(thickness = 2.dp, color = Color.Black)

                        Column (
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.95f)
                                .verticalScroll(state = ScrollState(0))
                                .padding(top = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SelectAppsComponentForDialogs()
                        }
                    }
                }
            }
        }
    }
}