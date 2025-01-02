package com.adormantsakthi.holup.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.adormantsakthi.holup.R

@Composable
fun BottomNavBar(navController: NavController, selectedItemIndex: MutableState<Int>) {

    // Bottom Nav Bar
    BottomAppBar (
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 50.dp, end = 50.dp, bottom = 10.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(40.dp)),
        containerColor = MaterialTheme.colorScheme.onSurface,
        contentPadding = PaddingValues()
    ){
        Row (
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Absolute.Center
        ) {
            NavigationBarItem(
                selected = selectedItemIndex.value == 0,
                onClick = {
                    navController.navigate("stats")
                    selectedItemIndex.value = 0
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = Color.Transparent,
                    selectedTextColor = Color.Transparent,
                    disabledIconColor = Color.Transparent,
                    disabledTextColor = Color.Transparent,
                    unselectedIconColor = Color.Transparent,
                    unselectedTextColor = Color.Transparent,
                    selectedIndicatorColor = Color.Transparent
                ),
                icon = {
                    Image(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(R.drawable.chart_line_solid),
                        contentDescription = "Chart Line",
                        colorFilter = ColorFilter.tint(if (selectedItemIndex.value == 0) MaterialTheme.colorScheme.primary else Color.Black)
                    )
                }
            )

            NavigationBarItem(
                selected = selectedItemIndex.value == 1,
                onClick = {
                    navController.navigate("home")
                    selectedItemIndex.value = 1
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = Color.Transparent,
                    selectedTextColor = Color.Transparent,
                    disabledIconColor = Color.Transparent,
                    disabledTextColor = Color.Transparent,
                    unselectedIconColor = Color.Transparent,
                    unselectedTextColor = Color.Transparent,
                    selectedIndicatorColor = Color.Transparent
                ),
                icon = {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(modifier = Modifier
                            .size(35.dp),
                            imageVector = Icons.Rounded.Home,
                            contentDescription = "Home",
                            tint = (if (selectedItemIndex.value == 1) MaterialTheme.colorScheme.primary else Color.Black))
                    }
                }
            )

            NavigationBarItem(
                selected = selectedItemIndex.value == 2,
                onClick = {
                    navController.navigate("settings")
                    selectedItemIndex.value = 2
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = Color.Transparent,
                    selectedTextColor = Color.Transparent,
                    disabledIconColor = Color.Transparent,
                    disabledTextColor = Color.Transparent,
                    unselectedIconColor = Color.Transparent,
                    unselectedTextColor = Color.Transparent,
                    selectedIndicatorColor = Color.Transparent
                ),
                icon = {
                    Icon(modifier = Modifier.size(30.dp), imageVector = Icons.Rounded.Settings, contentDescription = "Settings", tint = (if (selectedItemIndex.value == 2) MaterialTheme.colorScheme.primary else Color.Black))
                }
            )
        }
    }
}