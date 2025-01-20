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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import com.patrykandpatrick.vico.compose.common.getDefaultColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SetupAppsToLimitDialog(
    showDialog: MutableState<Boolean>,
    isAppBarVisible: MutableState<Boolean>,
    selectedItemIndex: MutableState<Int>,
) {

    val allApps = remember { mutableStateOf<List<Triple<String, Drawable, ApplicationInfo>>>(emptyList()) }
    val filteredApps = remember { mutableStateOf<List<Triple<String, Drawable, ApplicationInfo>>>(emptyList()) }
    val searchQuery = remember { mutableStateOf("") }
    val context = LocalContext.current



    if (showDialog.value) {

        // Load all apps once when the composable is launched
        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                val apps = GetDownloadedApps(context)
                allApps.value = apps
                filteredApps.value = apps
            }
        }

        // Update filtered apps whenever searchQuery changes
        LaunchedEffect(searchQuery.value) {
            CoroutineScope(Dispatchers.IO).launch {
                filteredApps.value = emptyList()
                filteredApps.value = allApps.value.filter { app ->
                    app.first.contains(searchQuery.value, ignoreCase = true)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .imePadding()
        ) {
            Column(
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
                        .aspectRatio(1 / 1.8f)
                        .background(Color.DarkGray)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
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

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max)
                                .padding(20.dp)
                                .clip(RoundedCornerShape(30.dp))
                                .background(MaterialTheme.colorScheme.secondary)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null,
                                    tint = Color.Black
                                )

                                TextField(
                                    value = searchQuery.value,
                                    onValueChange = { text -> searchQuery.value = text },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        cursorColor = MaterialTheme.colorScheme.primary
                                    ),
                                )
                            }
                        }
                        if (filteredApps.value.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.95f)
                                    .padding(top = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                itemsIndexed(items = filteredApps.value, key = { index, item ->
                                    // Combine index and package name for a unique key
                                    "index_${index}_${item.third.packageName}"
                                }) { _, item ->
                                    SelectAppsComponentForDialogs(item.first, item.second, item.third)
                                }
                            }
                        } else if(filteredApps.value.isEmpty() && searchQuery.value.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Transparent)
                                    .imePadding(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "No apps found",
                                    style = MaterialTheme.typography.labelLarge.copy(Color.White),
                                    modifier = Modifier
                                        .padding(20.dp)
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 6.dp,
                                    modifier = Modifier
                                        .size(100.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        // clear search query when leaving the dialog
        searchQuery.value = ""
    }
}
