package com.adormantsakthi.holup.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.ui.components.forStatistics.Graph
import com.adormantsakthi.holup.ui.components.forStatistics.TimeUsageForApp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import kotlin.math.exp

@Composable
fun Statistics(onNavigate: () -> Unit) {

    var expanded = remember { mutableStateOf(false) }
    var items = remember { mutableListOf("This week", "Last Week", "This Month") }
    var selectedOption = remember { mutableStateOf(items.firstOrNull() ?: "None") }

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
                Graph()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, top = 10.dp),
                    horizontalArrangement = Arrangement.End
                ){
                    Button(
                        onClick = {expanded.value = true},
                    ) {
                        Text(
                            selectedOption.value,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Box(
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false },
                            modifier = Modifier

                                .background(Color.LightGray)
                        ) {
                            items.forEach{ item ->
                                DropdownMenuItem(
                                    text = { Text(
                                        item,
                                        style = MaterialTheme.typography.labelSmall
                                    ) },
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

        // box for the Daily Average
        Box(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth(0.85f)
                .aspectRatio(2/1f)
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Column {
                Text(
                    "Daily Average",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                )

                HorizontalDivider(thickness = 2.dp, color = Color.Black)

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "2hr 32mins",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }

        // box for the task completion rate
        Box(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth(0.85f)
                .aspectRatio(2/1f)
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Column {
                Text(
                    "Task Completion Rate",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                )

                HorizontalDivider(thickness = 2.dp, color = Color.Black)

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "69%",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
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