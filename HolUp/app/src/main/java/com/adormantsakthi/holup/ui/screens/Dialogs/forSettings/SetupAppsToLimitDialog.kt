package com.adormantsakthi.holup.ui.screens.Dialogs.forSettings

import GetDownloadedApps
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.ui.components.forSettings.SelectAppsComponentForDialogs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SetupAppsToLimitDialog (
    showDialog: MutableState<Boolean>,
    isAppBarVisible: MutableState<Boolean>,
    selectedItemIndex: MutableState<Int>,
) {

    val listOfApps = remember { mutableStateOf<List<Triple<String, Drawable, ApplicationInfo>>>(
        emptyList()
    ) }
    val context = LocalContext.current


    if (showDialog.value) {
        if (listOfApps.value.isNotEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state = ScrollState(0)),
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
                                showDialog.value = false
                                isAppBarVisible.value = true
                                selectedItemIndex.value = 2
                            }
                            .align(Alignment.Start)
                    )

                    Text(
                        "Apps To Limit",
                        style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)
                    )

                    Box(
                        modifier = Modifier
                            .padding(top = 30.dp, bottom = 30.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .fillMaxWidth(0.95f)
                            .aspectRatio(1/1.8f)
                            .background(Color.DarkGray)
                    ) {
                        Column (
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Select Apps",
                                style = MaterialTheme.typography.labelMedium.copy(Color.White),
                                modifier = Modifier
                                    .padding(20.dp)
                                    .align(Alignment.Start)
                            )

                            HorizontalDivider(thickness = 2.dp, color = Color.Black)

                            LazyColumn (
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.95f)
                                    .padding(top = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                items(listOfApps.value){
                                        item -> SelectAppsComponentForDialogs(item.first, item.second, item.third)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            LaunchedEffect (Unit) {
                CoroutineScope(Dispatchers.IO).launch {
                    // Get the list of installed applications
                    listOfApps.value = GetDownloadedApps(context)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp),
                    strokeWidth = 6.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }


    }
}