package com.adormantsakthi.holup.ui.components.forInterruption

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adormantsakthi.holup.functions.Todo
import com.adormantsakthi.holup.ui.theme.Fresca

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun TaskBoxInInterruption(
    task: Todo,
) {
    // Task outer box
    Box(
        modifier = Modifier
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth(0.90f)
            .aspectRatio(3/0.8f)

    ) {
        // Main content
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Text(
                task.title,
                modifier = Modifier
                    .padding(10.dp),
                style = TextStyle(
                    fontFamily = Fresca,
                    fontSize = 24.sp,
                    color = Color.Black
                )
            )
        }
    }

    Spacer(Modifier.height(20.dp))
}