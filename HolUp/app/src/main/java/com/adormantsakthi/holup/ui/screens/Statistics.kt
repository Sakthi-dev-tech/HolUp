package com.adormantsakthi.holup.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.ui.Todo.TodoViewModel
import com.adormantsakthi.holup.ui.components.forStatistics.Graph
import com.adormantsakthi.holup.ui.components.forStatistics.TimeUsageForApp

@Composable
fun Statistics(onNavigate: () -> Unit) {

    var expanded = remember { mutableStateOf(false) }
    var items = remember { mutableListOf("This Week", "Last Week", "This Month") }
    var selectedOption = remember { mutableStateOf(items.firstOrNull() ?: "This Week") }

    // Example time spent (e.g., 8 hours 45 minutes out of 24 hours)
    val dailyAverageTimeSpentMinutes = 525 // minutes (8 hours 45 minutes)
    val totalMinutesInDay = 1440 // Total minutes in a day (24 hours)

    // Convert time spent into a percentage for progress
    val dailyAverageTimeSpentPercentage = dailyAverageTimeSpentMinutes / totalMinutesInDay.toFloat()

    // Dummy value for Task Completion Rate
    val totalNumOfTodos = TodoViewModel().totalNumOfTodos.observeAsState(0).value
    val totalNumOfCompletedTodos = TodoViewModel().totalNumOfCompletedTodos.observeAsState(0).value
    // Ensure we don't divide by zero and calculate the completion rate
    val taskCompletionRate = if (totalNumOfTodos != 0) {
        (totalNumOfCompletedTodos.toFloat() / totalNumOfTodos.toFloat())
    } else {
        0f // Avoid division by zero, return 0% completion rate
    }


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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
        ){
            // Column that contains the graph and the row inside it will contain the button and dropdown menu
            Column {
                Graph(selectedOption.value)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, top = 10.dp),
                    horizontalArrangement = Arrangement.End
                ){
                    Button(
                        onClick = { expanded.value = true },
                    ) {
                        Text(
                            selectedOption.value,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    // Box for positioning the DropdownMenu
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(10.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false },
                            modifier = Modifier
                                .background(Color.DarkGray)
                        ) {
                            items.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            item,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    },
                                    onClick = {
                                        selectedOption.value = item
                                        expanded.value = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Daily Average Time Spent KPI
            KPIStatWithTime(
                label = "Avg Time Spent\nPer Day",
                progress = dailyAverageTimeSpentPercentage,
                timeSpentMinutes = dailyAverageTimeSpentMinutes,
                color = Color.Red,
            )

            // Task Completion Rate KPI
            KPIStat(
                label = "Avg Task\nCompletion Rate",
                progress = taskCompletionRate,
                color = Color.Green
            )
        }

        // box for the time usage for the day
        Box(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth(0.85f)
                .aspectRatio(2/3f)
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Column {
                Text(
                    "Apps Usage For The Day",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                )

                HorizontalDivider(thickness = 2.dp, color = Color.Black)

                Column (
                    modifier = Modifier
                        .verticalScroll(state = ScrollState(0))
                ) {
                    TimeUsageForApp(Icons.Outlined.Star, "Spotify", "2hr 23mins")
                    TimeUsageForApp(Icons.Outlined.Star, "Spotify", "2hr 23mins")
                    TimeUsageForApp(Icons.Outlined.Star, "Spotify", "2hr 23mins")
                    TimeUsageForApp(Icons.Outlined.Star, "Spotify", "2hr 23mins")
                    TimeUsageForApp(Icons.Outlined.Star, "Spotify", "2hr 23mins")
                    TimeUsageForApp(Icons.Outlined.Star, "Spotify", "2hr 23mins")
                    TimeUsageForApp(Icons.Outlined.Star, "Spotify", "2hr 23mins")
                    TimeUsageForApp(Icons.Outlined.Star, "Spotify", "2hr 23mins")
                }
            }
        }

        Spacer(Modifier.height(125.dp))
    }
}

@Composable
fun KPIStatWithTime(
    label: String,
    progress: Float,
    timeSpentMinutes: Int,
    color: Color
) {
    val hours = timeSpentMinutes / 60
    val minutes = timeSpentMinutes % 60
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Circular Progress Indicator with Text in the center
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(100.dp)
        ) {
            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxSize(),
                strokeWidth = 10.dp,
                color = color,
                trackColor = Color.LightGray
            )
            // Text in the center of the progress indicator
            Text(
                text = String.format("%02d h:%02d m", hours, minutes),
                style = MaterialTheme.typography.labelSmall
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = label, textAlign = TextAlign.Center)
    }
}

@Composable
fun KPIStat(
    label: String,
    progress: Float,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Circular Progress Indicator with Text in the center
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(100.dp)
        ) {
            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxSize(),
                strokeWidth = 10.dp,
                color = color,
                trackColor = Color.LightGray
            )
            // Text in the center of the progress indicator
            Text(
                text = String.format("%.0f%%", progress * 100),
                style = MaterialTheme.typography.labelSmall
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, textAlign = TextAlign.Center)
    }
}
