package com.adormantsakthi.holup.ui.screens

import OnboardingPrefs
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.ui.screens.OnBoardingScreens.OnboardingScreen1
import com.adormantsakthi.holup.ui.screens.OnBoardingScreens.OnboardingScreen2
import com.google.accompanist.pager.HorizontalPagerIndicator

@Composable
fun OnboardingScreens(
    showOnboardingScreens: MutableState<Boolean>,
    showOnboardingScreensAgain: Boolean,
    isAppBarVisible: MutableState<Boolean>,
    showSkipButton: MutableState<Boolean>,
) {
    val pagerState = rememberPagerState(0, pageCount = { 2 }, initialPageOffsetFraction = 0f)
    val scrollScope = rememberCoroutineScope()
    val context = LocalContext.current

    if (showOnboardingScreens.value && showOnboardingScreensAgain) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize(0.85f)
            ) { page ->
                when (page) {
                    0 -> OnboardingScreen1()
                    1 -> OnboardingScreen2(showSkipButton)
                }
            }

            if (showSkipButton.value) {
                Button(
                    onClick = {
                        OnboardingPrefs.setOnboardingCompleted(context = context)
                        showOnboardingScreens.value = false
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 10.dp)
                ) {
                    Text(
                        "Skip"
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(Color.White, shape = CircleShape)
                    .clip(RoundedCornerShape(30.dp))
            ){
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    pageCount = 2,
                    activeColor = MaterialTheme.colorScheme.primary,
                    inactiveColor = Color.Black,
                    modifier = Modifier
                        .padding(5.dp),
                    indicatorHeight = 10.dp,
                    indicatorWidth = 10.dp,
                )
            }
        }
    }
}