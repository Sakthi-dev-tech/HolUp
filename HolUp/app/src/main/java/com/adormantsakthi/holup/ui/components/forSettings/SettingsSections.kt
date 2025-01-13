package com.adormantsakthi.holup.ui.components.forSettings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
    onClick: () -> Unit,
) {
    val customRipple = ripple(color = MaterialTheme.colorScheme.primary)
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary)
            .padding(vertical = if (content != null) 10.dp else 20.dp, horizontal = 10.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = null,
                indication = customRipple,
                enabled = true,
                onClickLabel = null,
                role = null,
                onClick = onClick
            ),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.primary),
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 10.dp).background(MaterialTheme.colorScheme.secondary)
        )

        if (content != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.background),
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
    }

    HorizontalDivider(color = MaterialTheme.colorScheme.background)
}