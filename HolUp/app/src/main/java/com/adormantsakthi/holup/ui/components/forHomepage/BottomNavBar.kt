package com.adormantsakthi.holup.ui.components.forHomepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.adormantsakthi.holup.R
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun BottomNavBar(navController: NavController) {
    // For state of selected index
    var selectedItemIndex by remember { mutableIntStateOf(1) }

    // Bottom Nav Bar
    BottomAppBar (
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 30.dp, end = 30.dp, bottom = 10.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(20.dp)),
        containerColor = MaterialTheme.colorScheme.onSurface,
        contentPadding = PaddingValues()
    ){
        Row (
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Absolute.Center
        ) {
            NavigationBarItem(
                selected = selectedItemIndex == 0,
                onClick = {
                    navController.navigate("stats")
                    selectedItemIndex = 0
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
                        colorFilter = ColorFilter.tint(if (selectedItemIndex == 0) MaterialTheme.colorScheme.primary else Color.Black)
                    )
                }
            )

            NavigationBarItem(
                selected = selectedItemIndex == 1,
                onClick = {
                    navController.navigate("home")
                    selectedItemIndex = 1
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
                    Icon(modifier = Modifier.size(35.dp), imageVector = Icons.Rounded.Home, contentDescription = "Home", tint = (if (selectedItemIndex == 1) MaterialTheme.colorScheme.primary else Color.Black))
                }
            )

            NavigationBarItem(
                selected = selectedItemIndex == 2,
                onClick = {
                    navController.navigate("settings")
                    selectedItemIndex = 2
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
                    Icon(modifier = Modifier.size(30.dp), imageVector = Icons.Rounded.Settings, contentDescription = "Settings", tint = (if (selectedItemIndex == 2) MaterialTheme.colorScheme.primary else Color.Black))
                }
            )
        }
    }
}