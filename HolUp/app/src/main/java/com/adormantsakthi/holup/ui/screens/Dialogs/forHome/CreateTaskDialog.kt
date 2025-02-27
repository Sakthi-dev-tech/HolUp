package com.adormantsakthi.holup.ui.screens.Dialogs.forHome

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.ui.Todo.TodoViewModel

@Composable
fun CreateTaskDialog (
    showDialog: MutableState<Boolean>,
    isAppBarVisible: MutableState<Boolean>,
) {
    val context = LocalContext.current

    if (showDialog.value) {
        val taskName = remember { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    showDialog.value = false
                    isAppBarVisible.value = true
                }
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.80f)
                    .aspectRatio(1/0.8f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .pointerInput(Unit) {
                        detectTapGestures {  }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Enter Your Task",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    )

                    TextField(
                        value = taskName.value,
                        onValueChange = { newText: String -> taskName.value = newText},
                        placeholder = { "Your Task" },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                        )
                    )

                    ElevatedButton(
                        onClick = {
                            if (taskName.value.length >= 6) {
                                showDialog.value = false
                                isAppBarVisible.value = true
                                TodoViewModel().addTodo(taskName.value)
                            } else {
                                Toast.makeText(context, "Create a Task with a minimum of 6 characters", Toast.LENGTH_SHORT).show()
                            }

                        },
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.onSurface,
                            contentColor = Color.White,
                            disabledContainerColor = MaterialTheme.colorScheme.secondary,
                            disabledContentColor = Color.White
                        ),
                        modifier = Modifier
                    ) {
                        Text(
                            "Submit",
                            style = MaterialTheme.typography.labelSmall.copy(Color.White)
                        )
                    }
                }
            }
        }
    }
}