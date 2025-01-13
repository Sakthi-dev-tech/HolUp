package com.adormantsakthi.holup.ui.components.forSettings

import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.OverlayStateManager
import com.adormantsakthi.holup.ui.Todo.MainApplication
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun SelectAppsComponentForDialogs (
    name: String,
    icon: Drawable,
    appInfo: ApplicationInfo
) {

    val context = LocalContext.current
    val packageName = appInfo.packageName
    var checked = remember { mutableStateOf(OverlayStateManager(context).getPackageManager().containsPackage(packageName)) }
    val userHasPlus = MainApplication.getInstance().billingManager.isSubscribed.collectAsState().value

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth(0.95f)
            .aspectRatio(1/0.35f)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row (
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked.value,
                onCheckedChange = {
                    if (checked.value) {
                        // if it was previously checked
                        OverlayStateManager(context).getPackageManager().removePackage(packageName)
                        checked.value = it
                    } else {
                        // if it was previously not checked
                        if (OverlayStateManager(context).getPackageManager().getTargetPackages().size == 2 && !userHasPlus) {
                            // if 2 apps are already selected if user has no Plus
                            Toast.makeText(context, "Only 2 apps can be limited for non-Plus users! Sorry :(", Toast.LENGTH_LONG).show()
                        } else {
                            // user can have more than 2 apps selected since user got Plus
                            OverlayStateManager(context).getPackageManager().addPackage(packageName)
                            checked.value = it
                        }
                    }
                                  },
                Modifier
                    .scale(1.2f)
                    .padding(start = 10.dp),
                colors = CheckboxDefaults.colors(
                    uncheckedColor = Color.Black,
                    checkedColor = MaterialTheme.colorScheme.background,
                    checkmarkColor = MaterialTheme.colorScheme.secondary
                )
            )

            Column (
                modifier = Modifier
                    .width(100.dp)
                    .padding(end = 20.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = rememberDrawablePainter(drawable = icon),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(35.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Text(
                    name,
                    style = MaterialTheme.typography.labelSmall.copy(textAlign = TextAlign.Center),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }

    Spacer(Modifier.height(10.dp))
}