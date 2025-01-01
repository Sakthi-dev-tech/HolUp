package com.adormantsakthi.holup.ui.components.forSettings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsSection (
    title: String,
    content: String?,
    onClick: () -> Unit
) {
    val customRipple = ripple(color = MaterialTheme.colorScheme.primary)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = null,
                indication = customRipple,
                enabled = true,
                onClickLabel = null,
                role = null,
                onClick = {}
            ) // Make the section clickable
            .padding(if (content != null) 10.dp else 20.dp), // Add more spacing when there is no subtext
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = Color.DarkGray,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 10.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (content != null) {
            Text(
                text = content,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }

    HorizontalDivider(color = Color.DarkGray)
}