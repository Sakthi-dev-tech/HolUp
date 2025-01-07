package com.adormantsakthi.holup.ui.screens

import android.provider.CalendarContract.Colors
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adormantsakthi.holup.R
import com.adormantsakthi.holup.TodoViewModel
import com.adormantsakthi.holup.ui.components.forHomepage.TaskBox
import com.adormantsakthi.holup.ui.components.forInterruption.TaskBoxInInterruption
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@Composable
fun InterruptionScreen(
    onDismiss: () -> Unit
) {
    // State to track if the Lottie animation has completed
    val (offsetValue, setOffsetValue) = remember { mutableStateOf(IntOffset(0, 0)) }
    var triggerFunction by remember { mutableStateOf(false) }

    // Red screen that will slide up after animation ends
    val slidingUpOffset by animateIntOffsetAsState(
        targetValue = offsetValue,
        animationSpec = tween(durationMillis = 1000) // Set duration of the slide-up
    )

    // State to manage the fading of content and buttons
    var contentAlpha by remember { mutableStateOf(0f) }
    var buttonsAlpha by remember { mutableStateOf(0f) }

    // This is where there will be the task list and the buttons to continue or not
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Red Box animation that slides up
        Box(
            modifier = Modifier
                .offset(slidingUpOffset.x.dp, slidingUpOffset.y.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(136, 7, 25, 255)),
                contentAlignment = Alignment.Center
            ) {
                HolUpAnimation(
                    onAnimationFinished = {
                        triggerFunction = true
                    }
                )
            }
        }

        // Main Content Column (Tasks list)
        val columnAlpha by animateFloatAsState(
            targetValue = contentAlpha,
            animationSpec = tween(durationMillis = 500) // Duration for fading in the content
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(columnAlpha)
                .background(Color.Black), // Apply alpha for fade out effect
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(500.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Pending Tasks",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(15.dp)
                    )

                    // Convert LiveData into State for Compose
                    val todoList by TodoViewModel().todoList.observeAsState(emptyList())
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.9f)
                            .clip(RoundedCornerShape(20.dp))
                            .verticalScroll(state = ScrollState(0))
                            .background(Color(52, 49, 49, 255)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        todoList.forEach { todo ->
                            TaskBoxInInterruption(todo)
                        }
                    }
                }
            }
            // Fade-in effect for buttons after delay
            val buttonsAlphaAnim by animateFloatAsState(
                targetValue = buttonsAlpha,
                animationSpec = tween(durationMillis = 500) // Duration for fading in buttons
            )

            // Wait for animation to finish and fade in buttons
            LaunchedEffect(triggerFunction) {
                if (triggerFunction) {
                    delay(1500)
                    // Slide the red box up and start fading out content
                    setOffsetValue(IntOffset(0, -3000)) // Slide red box up

                    // Fade out content
                    delay(1000) // Optional delay before fading content
                    contentAlpha = 1f // Start fading in content

                    // Fade in buttons after 1 second
                    delay(1000) // Delay before buttons fade in
                    buttonsAlpha = 1f
                }
            }

            // Buttons Row
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .alpha(buttonsAlphaAnim), // Apply alpha for fade-in
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .width(150.dp)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(79, 79, 79, 255)
                    )
                ) {
                    Text(
                        "Continue",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .width(150.dp)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text(
                        "Leave",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
fun HolUpAnimation(onAnimationFinished: () -> Unit) {
    val animatable = rememberLottieAnimatable()
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.holuplogoanim))

    // Use LaunchedEffect to play the Lottie animation and call the callback on finish
    LaunchedEffect(composition) {
        animatable.animate(composition, iterations = 1)
        // Once animation finishes, trigger the callback
        onAnimationFinished()
    }

    // Lottie Animation loading or actual animation
    if (composition != null) {
        LottieAnimation(composition = composition, modifier = Modifier.size(150.dp))
    } else {
        CircularProgressIndicator(
            color = Color.Black, // Custom color
            modifier = Modifier
                .size(80.dp), // Custom size
            strokeWidth = 6.dp // Custom stroke width
        )
    }
}
