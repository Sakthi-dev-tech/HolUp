package com.adormantsakthi.holup.ui.screens

import GetDownloadedApps
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.storage.LimitedAppsStorage
import com.adormantsakthi.holup.ui.Todo.MainApplication
import com.adormantsakthi.holup.ui.Todo.TodoViewModel
import com.adormantsakthi.holup.ui.components.forStatistics.Graph
import com.adormantsakthi.holup.ui.components.forStatistics.TimeUsageForApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Statistics(onNavigate: () -> Unit, selectedItemIndex: MutableIntState) {

    selectedItemIndex.intValue = 0

    val context = LocalContext.current

    val billingManager = MainApplication.getInstance().billingManager
    val isSubbed = billingManager.isSubscribed.collectAsState().value

    var expanded = remember { mutableStateOf(false) }
    var items = listOf("This Week", "Last Week")
    var selectedOption = remember { mutableStateOf(items.firstOrNull() ?: "This Week") }

    // Dummy value for Task Completion Rate
    val totalNumOfTodos = TodoViewModel().totalNumOfTodos.observeAsState(0).value
    val totalNumOfCompletedTodos = TodoViewModel().totalNumOfCompletedTodos.observeAsState(0).value
    // Ensure we don't divide by zero and calculate the completion rate
    val taskCompletionRate = if (totalNumOfTodos != 0) {
        (totalNumOfCompletedTodos.toFloat() / totalNumOfTodos.toFloat())
    } else {
        0f // Avoid division by zero, return 0% completion rate
    }
    val validTaskCompletionRate = taskCompletionRate.coerceIn(0f, 1f)

    // Get the applications with interruption set for it
    val listOfAppsWithInterruption = LimitedAppsStorage(context).getTargetPackages()
    val listOfApps = remember { mutableStateOf<List<Triple<String, Drawable, ApplicationInfo>>>(
        emptyList()
    ) }
    val filteredListOfAppsForInterruption = remember { mutableStateOf<List<Triple<String, Drawable, ApplicationInfo>>>(
        emptyList()
    ) }
    LaunchedEffect (Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            // Get the list of installed applications
            listOfApps.value = GetDownloadedApps(context)

            // Assuming `listOfAppsWithInterruption` is a Set<String> or List<String>
            filteredListOfAppsForInterruption.value = listOfApps.value.filter { triple ->
                val packageName = triple.third.packageName // Extract package name from ApplicationInfo
                packageName in listOfAppsWithInterruption
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = ScrollState(0))
                .blur( if (!isSubbed) 20.dp else 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Your Statistics",
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.secondary),
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

                // Task Completion Rate KPI
                KPIStat(
                    label = "Avg Task\nCompletion Rate",
                    progress = validTaskCompletionRate.takeIf { !it.isNaN() } ?: 0f,
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
                        filteredListOfAppsForInterruption.value.forEach({ app ->
                            TimeUsageForApp(app.second, app.first, app.third)
                        })
                    }
                }
            }

            Spacer(Modifier.height(125.dp))
        }

        if (!isSubbed) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .height(100.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color(255, 80,5))
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "Please Upgrade to Plus to View Your Statistics :)",
                    style = MaterialTheme.typography.labelMedium.copy(color = Color.White, textAlign = TextAlign.Center),
                    modifier = Modifier
                        .padding(20.dp),

                )
            }

        }
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
                trackColor = MaterialTheme.colorScheme.secondary
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
