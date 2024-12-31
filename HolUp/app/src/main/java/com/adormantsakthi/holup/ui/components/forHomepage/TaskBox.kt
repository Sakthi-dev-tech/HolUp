package com.adormantsakthi.holup.ui.components.forHomepage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskBox(
    task: String
) {
    val checkedState = remember { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current

    // Task outer box
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .aspectRatio(3/0.8f)
            .background(Color.White)
            .combinedClickable(
                onClick = { },
                onLongClick = {
                    // to edit/delete the current task
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    println("Long Press Detected!")
                }
            )
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

    Spacer(Modifier.height(20.dp
    ))
}

@Preview (showBackground = false)
@Composable
fun PreviewTaskBox() {
    TaskBox("Wash Clothes")
}
