package com.adormantsakthi.holup.ui.screens.Dialogs.forSettings

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.common.getDefaultColors

@Composable
fun FeedbackDialog(
    showFeedbackDialog: MutableState<Boolean>,
    isAppBarVisible: MutableState<Boolean>,
    selectedItemIndex: MutableState<Int>
) {

    val subject = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }
    val context = LocalContext.current

    if (showFeedbackDialog.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(Unit){}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(40.dp)
                        .clickable {
                            showFeedbackDialog.value = false
                            isAppBarVisible.value = true
                            selectedItemIndex.value = 2
                        }
                        .align(Alignment.Start)
                )

                Text(
                    "Subject",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 20.dp, start = 20.dp)
                )

                TextField(
                    value = subject.value,
                    onValueChange = {
                        subject.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.secondary,
                        unfocusedTextColor = MaterialTheme.colorScheme.secondary
                    )
                )

                Text(
                    "Message",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 20.dp, start = 20.dp)
                )

                TextField(
                    value = message.value,
                    onValueChange = {
                        message.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(20.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.secondary,
                        unfocusedTextColor = MaterialTheme.colorScheme.secondary
                    )
                )

                ElevatedButton(
                    onClick = {
                        if (subject.value.length >= 6 && message.value.length >= 20) {
                            sendEmail(subject.value, message.value, context)
                            showFeedbackDialog.value = false
                            isAppBarVisible.value = true
                            selectedItemIndex.value = 2
                            subject.value = ""
                            message.value = ""
                        } else if (subject.value.length < 6) {
                            Toast.makeText(context, "Subject must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                        } else if (message.value.length < 20) {
                            Toast.makeText(context, "Message must be at least 20 characters long", Toast.LENGTH_SHORT).show()
                        }
                              },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(20.dp)
                        .height(IntrinsicSize.Min),
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text(
                        "Send",
                        style = MaterialTheme.typography.labelMedium.copy(MaterialTheme.colorScheme.secondary)
                    )
                }
            }
        }
    }
}

fun sendEmail(subject: String, feedbackText: String, context: Context) {
    val recipient = "adormatsakthi@gmail.com" // Replace with your email address

    val emailIntent = Intent(Intent.ACTION_SEND).apply {
        type = "message/rfc822" // MIME type for email
        putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, feedbackText)
    }

    try {
        context.startActivity(Intent.createChooser(emailIntent, "Send Feedback"))
        Toast.makeText(context, "Directing to your email app...", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Something Went Wrong!", Toast.LENGTH_SHORT).show()
        Log.e("Send Email in Feedback Dialog", e.toString())
    }
}
