package com.adormantsakthi.holup.ui.screens

import OnboardingPrefs
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.ui.Todo.TodoViewModel
import com.adormantsakthi.holup.functions.Todo
import com.adormantsakthi.holup.functions.statistics.NumOfTimesLimitedAppsAccessedStorage
import com.adormantsakthi.holup.ui.screens.Dialogs.forHome.CreateTaskDialog
import com.adormantsakthi.holup.ui.screens.Dialogs.forHome.EditTaskDialog
import com.adormantsakthi.holup.ui.components.forHomepage.TaskBox
import com.adormantsakthi.holup.ui.theme.Karma
import java.util.Calendar

@Composable
fun Homescreen(onNavigate: () -> Unit, isAppBarVisible: androidx.compose.runtime.MutableState<Boolean>) {

    val showCreateTaskDialog = remember { mutableStateOf(false) }
    val showEditTaskDialog = remember { mutableStateOf(false) }
    val selectedTask = remember { mutableStateOf<Todo?>(null) }

    val showOnboardingScreensAgain = !OnboardingPrefs.isOnboardingCompleted(context = LocalContext.current)
    val showOnboardingScreens = remember { mutableStateOf(true) } // this is so that we can immediately close the onboarding screens once skip pressed

    if (showOnboardingScreens.value && showOnboardingScreensAgain) {
        isAppBarVisible.value = false
    } else {
        isAppBarVisible.value = true
    }

    val numOfTimesLimitedAppsAccessed = NumOfTimesLimitedAppsAccessedStorage.getNumberOfTimes(LocalContext.current)

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
                            .padding(15.dp)
                    )

                    Icon(
                        imageVector = Icons.Outlined.AddCircle,
                        contentDescription = "Add Task Icon",
                        tint = Color.Black, // Icon color
                        modifier = Modifier
                            .size(60.dp) // Icon size
                            .padding(10.dp)
                            .clickable (
                                indication = ripple(
                                    bounded = false
                                ),
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                showCreateTaskDialog.value = true
                                isAppBarVisible.value = false
                            }
                    )
                }
                HorizontalDivider(color = Color.Black, thickness = 2.dp)

                // to space out between the line and the tasks
                Spacer(Modifier.height(10.dp))

                Column (
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight()
                        .padding()
                        .verticalScroll(ScrollState(0))
                ) {
                    // Convert LiveData into State for Compose
                    val todoList by TodoViewModel().todoList.observeAsState(emptyList())

                    todoList.forEach { value ->
                        TaskBox(value, showEditTaskDialog, selectedTask, isAppBarVisible)
                    }
                }
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
                    numOfTimesLimitedAppsAccessed.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = Karma,
                    modifier = Modifier
                        .align(alignment = Alignment.End)
                        .padding(20.dp)
                )
            }
        }

        // Spacer for slight scroll
        Spacer(Modifier.height(150.dp))
    }

    CreateTaskDialog(showCreateTaskDialog, isAppBarVisible)
    EditTaskDialog(selectedTask.value, showEditTaskDialog, isAppBarVisible)
    OnboardingScreens(showOnboardingScreens, showOnboardingScreensAgain, isAppBarVisible, remember { mutableStateOf(false) })
}