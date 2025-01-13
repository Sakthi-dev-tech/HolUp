package com.adormantsakthi.holup.ui.screens.Dialogs.forSettings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.ui.Todo.MainApplication
import com.adormantsakthi.holup.ui.components.forSettings.PurchaseHistoryComponent

@Composable
fun PurchaseHistoryDialog(
    showDialog: MutableState<Boolean>,
    isAppBarVisible: MutableState<Boolean>,
    billingHistory: MutableState<List<Pair<String, Long>>>,
    selectedItemIndex: MutableState<Int>
) {
    if (showDialog.value) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column (
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
                            showDialog.value = false
                            isAppBarVisible.value = true
                            selectedItemIndex.value = 2
                        }
                        .align(Alignment.Start)
                )
                LazyColumn (
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(1f)
                        .padding(20.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(billingHistory.value) {
                            purchase -> PurchaseHistoryComponent(purchase.first, purchase.second)
                    }
                }
            }
        }
    }
}