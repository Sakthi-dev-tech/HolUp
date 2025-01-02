package com.adormantsakthi.holup.ui.components.Dialogs.forSettings

import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adormantsakthi.holup.R
import com.adormantsakthi.holup.ui.components.Dialogs.forSettings.forUpgradeDialog.MonthlyPlan
import com.adormantsakthi.holup.ui.components.Dialogs.forSettings.forUpgradeDialog.YearlyPlan
import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.launch

@Composable
fun UpgradeToProDialog(
    showDialog: MutableState<Boolean>,
    isAppBarVisible: MutableState<Boolean>,
    selectedItemIndex: MutableState<Int>
) {

    // pager state
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = {3}
    )

    // Create a coroutine scope to launch suspend functions
    val scope = rememberCoroutineScope()

    if (showDialog.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
                .clickable (
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    showDialog.value = false
                    isAppBarVisible.value = true
                    selectedItemIndex.value = 2
                    scope.launch {
                        pagerState.scrollToPage(0)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1/1.5f)
                    .background(MaterialTheme.colorScheme.primary)
                    .pointerInput(Unit) {
                        detectTapGestures {  }
                    }
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Upgrade Your Focus",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Italic),
                    )

                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxSize(0.95f),
                        state = pagerState
                    ) { page ->
                        when (page) {
                            0 -> {
                                ComparisonChart()
                            }

                            1 -> {
                                MonthlyPlan()
                            }

                            2 -> {
                                YearlyPlan()
                            }
                        }
                    }

                    Spacer(Modifier.height(5.dp))

                    HorizontalPagerIndicator(
                        pageCount = 3,
                        pagerState = pagerState,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 3.dp)
                    )

                }
            }
        }
    }
}