package com.adormantsakthi.holup.ui.screens

import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.ui.theme.Karma

@Composable
fun Homescreen(onNavigate: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = ScrollState(0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Welcome Home,",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 15.dp)
                .fillMaxWidth()
        )

        // Pending Tasks for Today Box
        Box(
            modifier = Modifier
                .padding(bottom = 40.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth(0.8f)
                .aspectRatio(0.67f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                // Row for Tasks header and add button
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Tasks for today",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .padding(10.dp)
                    )

                    Icon(
                        imageVector = Icons.Outlined.AddCircle,
                        contentDescription = "Add Task Icon",
                        tint = Color.Black, // Icon color
                        modifier = Modifier
                            .size(60.dp) // Icon size
                            .padding(10.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .height(2.dp)
                )
            }
        }

        // Number of times limited apps accessed Box
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(40.dp))
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth(0.8f)
                .aspectRatio(1.48f)
        ) {
            Column {
                Text(
                    "Number of times you accessed limited apps",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                )

                Text(
                    "12",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = Karma,
                    modifier = Modifier
                        .align(alignment = Alignment.End)
                        .padding(20.dp)
                )
            }
        }

        // Spacer for slight scroll
        Spacer(Modifier.height(100.dp))
    }
}