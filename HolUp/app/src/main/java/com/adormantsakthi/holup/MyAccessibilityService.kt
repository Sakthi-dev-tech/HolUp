package com.adormantsakthi.holup

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.adormantsakthi.holup.ui.screens.InterruptionScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OverlayStateManager {
    // Tracks whether the overlay should be visible
    private val _isOverlayVisible = MutableStateFlow(false)
    val isOverlayVisible: StateFlow<Boolean> = _isOverlayVisible

    // Tracks which apps should trigger the overlay
    private val targetPackages = setOf(
        "com.google.android.youtube"
        // Add other package names here
    )

    // Called when a new app is detected
    fun onAppOpened(packageName: String) {
        if (packageName in targetPackages && !isOverlayVisible.value) {
            _isOverlayVisible.value = true
        }
    }

    // Called when user takes action to dismiss
    fun dismissOverlay() {
        _isOverlayVisible.value = false
    }
}

class MyAccessibilityService : AccessibilityService(), LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {
    // Lifecycle components
    private val lifecycleRegistry = LifecycleRegistry(this)
    override  val lifecycle: Lifecycle get() = lifecycleRegistry
    override val viewModelStore = ViewModelStore()
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    // Implement savedStateRegistry as a property
    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    // Window management
    private var windowManager: WindowManager? = null
    private var overlayView: ComposeView? = null


    override fun onServiceConnected() {
        super.onServiceConnected()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // Initialize lifecycle components
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED

        // Start observing overlay state
        CoroutineScope(Dispatchers.Main).launch {
            overlayStateManager.isOverlayVisible.collect { shouldShow ->
                if (shouldShow) {
                    delay(500)
                    showOverlay()
                } else {
                    hideOverlay()
                }
            }
        }
    }

    // Add these properties to track state
    private var overlayJob: Job? = null
    private val overlayStateManager = OverlayStateManager()

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return
            overlayStateManager.onAppOpened(packageName)
            Log.d("App Currently Open", packageName)
        }
    }

    private fun showOverlay() {
        if (overlayView != null) return

        overlayView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@MyAccessibilityService)
            setViewTreeViewModelStoreOwner(this@MyAccessibilityService)
            setViewTreeSavedStateRegistryOwner(this@MyAccessibilityService)

            setContent {
                InterruptionScreen(
                    onDismiss = {
                        overlayStateManager.dismissOverlay()
                    }
                )
            }
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            // Make sure overlay can receive touch events
            flags = flags and WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE.inv()
        }

        try {
            windowManager?.addView(overlayView, params)
        } catch (e: Exception) {
            Log.e("MyAccessibilityService", "Error showing overlay", e)
        }
    }

    private fun hideOverlay() {
        try {
            overlayView?.let { view ->
                windowManager?.removeView(view)
                overlayView = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onInterrupt() {
        hideOverlay()
    }

    override fun onDestroy() {
        super.onDestroy()
        overlayJob?.cancel()
        hideOverlay()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        viewModelStore.clear()
    }
}