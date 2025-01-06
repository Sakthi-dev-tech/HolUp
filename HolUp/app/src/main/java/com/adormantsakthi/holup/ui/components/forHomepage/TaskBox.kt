package com.adormantsakthi.holup.ui.components.forHomepage

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.adormantsakthi.holup.TodoViewModel
import com.adormantsakthi.holup.functions.Todo
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@SuppressLint("UseOfNonLambdaOffsetOverload")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskBox(
    task: Todo,
    showEditTaskDialog: MutableState<Boolean>,
    selectedTask: MutableState<Todo?>
) {
    val checkedState = remember { mutableStateOf(task.isCompleted) }

    // for swipe animation
    val swipeOffset = remember { androidx.compose.animation.core.Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Task outer box
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .aspectRatio(3/0.8f)
            .clickable {  }
    ) {
        val maxButtonWidth = 80.dp
        val minButtonWidth = 50.dp
        val dynamicWidth = with(LocalDensity.current) {
            (swipeOffset.value.coerceIn(-200f, 0f).absoluteValue / 200f).let {
                progress -> (minButtonWidth + progress * (maxButtonWidth - minButtonWidth)).value.dp
            }
        }
        // Background options: Edit and Delete
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3/0.8f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(dynamicWidth)
                    .background(Color(30, 99, 11))
                    .clickable {
                        showEditTaskDialog.value = true
                        selectedTask.value = task
                    },
                contentAlignment = Alignment.Center
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize(),
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        tint = Color.White,
                        contentDescription = "Edit"
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        "Edit",
                        style = MaterialTheme.typography.labelSmall.copy(Color.White)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(dynamicWidth)
                    .background(Color(207, 6, 27))
                    .clickable {
                        TodoViewModel().deleteTodo(task.id)
                    },
                contentAlignment = Alignment.Center
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        tint = Color.White,
                        contentDescription = "Delete"
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        "Delete",
                        style = MaterialTheme.typography.labelSmall.copy(Color.White)
                    )
                }
            }
        }

        // Main content
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .offset(x = swipeOffset.value.dp)
                .background(Color.White)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                // Snap content to fully reveal options or close
                                if (swipeOffset.value < -100) {
                                    swipeOffset.animateTo(-140f)
                                } else {
                                    swipeOffset.animateTo(0f)
                                }
                            }
                        },

                        onHorizontalDrag = {_, dragAmount ->
                            scope.launch {
                                val newOffset = (swipeOffset.value + dragAmount).coerceIn(-140f, 0f)
                                swipeOffset.snapTo(newOffset)
                            }
                        }
                    )
                }
        ) {
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = {
                    checkedState.value = it
                    TodoViewModel().editTodo(task.id, task.title, checkedState.value) },
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
                task.title,
                modifier = Modifier
                    .padding(10.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }

    Spacer(Modifier.height(20.dp))
}