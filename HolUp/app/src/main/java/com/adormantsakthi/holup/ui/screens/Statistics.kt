package com.adormantsakthi.holup.ui.screens

import android.graphics.Paint.Align
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.ui.components.forStatistics.Graph
import com.adormantsakthi.holup.ui.components.forStatistics.TimeUsageForApp

@Composable
fun Statistics() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = ScrollState(0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Your Statistics",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 15.dp)
                .fillMaxWidth()
        )

        // Box that contains the graph
        Box(
            modifier = Modifier.padding(20.dp)
        ){
            Graph()
        }

        Spacer(Modifier.height(30.dp))

        // Daily Average Box
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(2/1f)
                .clip(RoundedCornerShape(40.dp))
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            Column(

            ) {
                Text(
                    "Daily Average",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(20.dp)
                )

                HorizontalDivider(
                    thickness = 2.dp
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        "3 hours 32 mins",
                        style = MaterialTheme.typography.labelLarge.copy(MaterialTheme.colorScheme.tertiary),
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }

        Spacer(Modifier.height(30.dp))

        // Task Completion Rate Box
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(2/1f)
                .clip(RoundedCornerShape(40.dp))
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            Column(

            ) {
                Text(
                    "Task Completion Rate",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(20.dp)
                )

                HorizontalDivider(
                    thickness = 2.dp
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        "69%",
                        style = MaterialTheme.typography.labelLarge.copy(MaterialTheme.colorScheme.tertiary),
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }

        Spacer(Modifier.height(30.dp))

        // Breakdown of time usage of apps for the day
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(40.dp))
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            Column(

            ) {
                Text(
                    "Time Usage For Today",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(20.dp)
                )

                HorizontalDivider(
                    thickness = 2.dp
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column (
                        Modifier.verticalScroll(state = ScrollState(0))
                    ) {
                        TimeUsageForApp(Icons.Outlined.AccountCircle, "Dummy", "2h 49mins")
                    }
                }
            }
        }

        Spacer(Modifier.height(150.dp))
    }
}