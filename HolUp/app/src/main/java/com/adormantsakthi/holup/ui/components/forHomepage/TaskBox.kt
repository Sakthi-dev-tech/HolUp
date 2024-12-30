package com.adormantsakthi.holup.ui.components.forHomepage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TaskBox(
    task: String
) {
    val checkedState = remember { mutableStateOf(false) }

    // Task outer box
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .aspectRatio(3/0.8f)
            .background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()

        ) {
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = { checkedState.value = it },
                colors = CheckboxColors(
                    checkedCheckmarkColor = MaterialTheme.colorScheme.primary,
                    checkedBoxColor = Color.Transparent,
                    checkedBorderColor = Color.Black,
                    uncheckedBoxColor = Color.Transparent,
                    uncheckedBorderColor = Color.Black,
                    uncheckedCheckmarkColor = Color.Transparent,
                    disabledBorderColor = Color.DarkGray,
                    disabledCheckedBoxColor = Color.DarkGray,
                    disabledUncheckedBoxColor = Color.Transparent,
                    disabledIndeterminateBoxColor = Color.Transparent,
                    disabledIndeterminateBorderColor = Color.Transparent,
                    disabledUncheckedBorderColor = Color.Transparent
                )
            )

            Text(
                task,
                modifier = Modifier
                    .padding(10.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview (showBackground = false)
@Composable
fun PreviewTaskBox() {
    TaskBox("Wash Clothes")
}
