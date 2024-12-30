package com.adormantsakthi.holup

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.ui.theme.HolUpTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.ui.graphics.Color
import com.adormantsakthi.holup.ui.components.forHomepage.BottomNavBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HolUpTheme {
                // Get the System UI Controller
                val systemUiController = rememberSystemUiController()

                // Set the status bar and nav bar colours
                systemUiController.setSystemBarsColor(MaterialTheme.colorScheme.background)
                systemUiController.setNavigationBarColor(MaterialTheme.colorScheme.background)

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavBar() },
                    content = {
                        paddingValues -> Box(
                            modifier = Modifier.padding(paddingValues)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                            content = {
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
                                                        .padding(10.dp)
                                                )

                                                Icon(
                                                    imageVector = Icons.Outlined.AddCircle,
                                                    contentDescription = "Add Task Icon",
                                                    tint = Color.Black, // Icon color
                                                    modifier = Modifier
                                                        .size(60.dp) // Icon size
                                                        .padding(10.dp)
                                                )
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color.Black)
                                                    .height(2.dp)
                                            )
                                        }
                                    }

                                    // Number of times limited apps accessed Box
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(40.dp))
                                            .background(MaterialTheme.colorScheme.primary)
                                            .fillMaxWidth(0.8f)
                                            .aspectRatio(1.48f)
                                    )

                                    // Spacer for slight scroll
                                    Spacer(Modifier.height(20.dp))
                                }
                            }
                        )
                    }
                )
            }
        }
    }
}