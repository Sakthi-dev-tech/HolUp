package com.adormantsakthi.holup.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.ui.components.forSettings.SettingsSection

@Composable
fun Settings(onNavigate: () -> Unit) {
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
                    .padding(vertical = 0.dp, horizontal = 15.dp)
                    .fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .fillMaxWidth(0.85f)
                    .aspectRatio(3/1f)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .clickable {  }
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
                        "Upgrade to HolUp! Pro",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(end = 10.dp),
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            Text(
                text = "Edit HolUp! Popup",
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
                SettingsSection("Pop-Up Text", "HolUp! You have these remaining tasks!", {})
                SettingsSection("Pop-up Duration", "Medium", {})
                SettingsSection("Delay Between App Switch", "1 minute", {})
            }

            Spacer(Modifier.height(30.dp))

            Text(
                text = "Anti-Doomscroll",
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
                SettingsSection("Re-Popup", null, {})
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
                SettingsSection("Add Apps", "Select Apps that you want to limit", {})
                SettingsSection("Accessibility Service", "On", {})
                SettingsSection("App Usage Permission", "On", {})
            }

            Spacer(Modifier.height(150.dp))
        }
    }
