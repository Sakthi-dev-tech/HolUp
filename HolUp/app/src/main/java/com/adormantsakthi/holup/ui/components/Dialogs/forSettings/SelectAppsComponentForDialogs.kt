package com.adormantsakthi.holup.ui.components.Dialogs.forSettings

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SelectAppsComponentForDialogs () {

    var checked = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth(0.95f)
            .aspectRatio(1/0.35f)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row (
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked.value,
                onCheckedChange = {checked.value = it},
                Modifier
                    .scale(1.2f)
                    .padding(start = 10.dp),
                colors = CheckboxDefaults.colors(
                    uncheckedColor = Color.Black,
                    checkedColor = Color.Black,
                    checkmarkColor = MaterialTheme.colorScheme.secondary
                )
            )

            Column (
                modifier = Modifier
                    .padding(end = 20.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Build,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(35.dp)
                )

                Text(
                    "App Name",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }

    Spacer(Modifier.height(10.dp))
}