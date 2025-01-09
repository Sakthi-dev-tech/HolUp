package com.adormantsakthi.holup

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.adormantsakthi.holup.storage.LimitedAppsStorage
import com.adormantsakthi.holup.storage.ReInterruptionStorage
import com.adormantsakthi.holup.ui.screens.InterruptionScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Timer

var lastExecutionTime: MutableLongState = mutableLongStateOf(0) // Timestamp of the last function execution
val delayBtwAppSwitch =  mutableIntStateOf(60000)
val overlayClosed = mutableStateOf(true)

// Tracks whether the overlay should be visible
private val _isOverlayVisible = MutableStateFlow(false)
val isOverlayVisible: StateFlow<Boolean> = _isOverlayVisible

class OverlayStateManager (context: Context) {
    // Initialize the package manager
    private val packageManager = LimitedAppsStorage(context)

    // Method to access the package manager for configuration
    fun getPackageManager(): LimitedAppsStorage = packageManager

    private val lock = Any() // To ensure thread safety if needed

    // Called when a new app is detected
    fun onAppOpened(packageName: String) {
        cancelTimer()

        if (overlayClosed.value){
            if (getPackageManager().containsPackage(packageName) && !isOverlayVisible.value) {

                synchronized(lock) {
                    val currentTime = System.currentTimeMillis()

                    if (currentTime - lastExecutionTime.longValue <= delayBtwAppSwitch.intValue) {
                        val remainingTime = (delayBtwAppSwitch.intValue - (currentTime - lastExecutionTime.longValue)) / 1000
                        Log.d("Overlay Timer", "Overlay on cooldown for $remainingTime more seconds")
                        return
                    }
                }

                _isOverlayVisible.value = true
                overlayClosed.value = false
            }
        }
    }

    // Called when user takes action to dismiss
    fun dismissOverlay() {
        if (_isOverlayVisible.value) {
            _isOverlayVisible.value = false
        }
    }


    private val handler = Handler(Looper.getMainLooper())
    private var timerRunnable: Runnable? = null

    /**
     * Starts or resets the timer.
     */
    fun startOrResetTimer() {
        // Cancel any existing timer
        timerRunnable?.let { handler.removeCallbacks(it) }

        // Create a new runnable to execute after the timeout
        timerRunnable = Runnable {
            _isOverlayVisible.value = true
            overlayClosed.value = false
        }

        // Post the runnable with the specified delay
        handler.postDelayed(timerRunnable!!, 5000)
    }

    /**
     * Cancels the timer.
     */
    fun cancelTimer() {
        timerRunnable?.let { handler.removeCallbacks(it) }
        timerRunnable = null
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
            isOverlayVisible.collect { shouldShow ->
                if (shouldShow) {
                    delay(1000)
                    showOverlay()
                } else {
                    overlayView?.let {
                        view ->
                        if (view.isAttachedToWindow){
                            view.animate()
                                .alpha(0f)
                                .setDuration(300)
                                .withEndAction{
                                    hideOverlay()
                                }.start()
                        } else {
                            hideOverlay()
                        }
                    }

                }
            }
        }
    }

    // Add these properties to track state
    private var overlayJob: Job? = null
    private val overlayStateManager = OverlayStateManager(this)
    private var openedPackageName: String = ""

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return
            CoroutineScope(Dispatchers.Default).launch {
                overlayStateManager.onAppOpened(packageName)
            }
            if (packageName != "com.adormantsakthi.holup") {
                openedPackageName = packageName
            }
            Log.d("App Currently Open", packageName)
        }
    }

    // Add function to close the current app
    private fun closeCurrentApp() {
        try {
            // Perform HOME action to minimize the current app
            performGlobalAction(GLOBAL_ACTION_HOME)
            // Dismiss the overlay
            overlayStateManager.dismissOverlay()
        } catch (e: Exception) {
            Log.e("MyAccessibilityService", "Error closing app", e)
        }
    }

    private fun showOverlay() {
        try {
            if (overlayView != null) return

            overlayView = ComposeView(this).apply {
                setViewTreeLifecycleOwner(this@MyAccessibilityService)
                setViewTreeViewModelStoreOwner(this@MyAccessibilityService)
                setViewTreeSavedStateRegistryOwner(this@MyAccessibilityService)

                setContent {
                    InterruptionScreen(
                        onDismiss = {
                            lastExecutionTime.longValue = System.currentTimeMillis()
                            overlayStateManager.dismissOverlay()
                            val handler = android.os.Handler(Looper.getMainLooper())
                            handler.postDelayed({
                                overlayClosed.value = true
                            }, 1000)

                            // starts AntiDoomscroll timer if app package name in Reinterruption Storage
                            Log.d("Opened Package Name Before If Func", openedPackageName)
                            CoroutineScope(Dispatchers.IO).launch {
                                if (ReInterruptionStorage(context).containsPackage(openedPackageName)) {
                                    Log.d("Opened Package Name", openedPackageName)
                                    OverlayStateManager(context).startOrResetTimer()
                                }
                            }
                        },

                        onClose = {
                            lastExecutionTime.longValue = 0
                            closeCurrentApp()
                            val handler = android.os.Handler(Looper.getMainLooper())
                            handler.postDelayed({
                                overlayClosed.value = true
                            }, 1000)
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
        } catch (e: Exception) {
            Log.e("Error while showOverlay in MyAccessibilityService", e.toString())
        }
    }

    private fun hideOverlay() {
        try {
            overlayView?.let { view ->
                windowManager?.removeView(view)
                overlayView = null
            }
        } catch (e: Exception) {
            Log.e("Error while hiding overlay in MyAccessibilityService", e.toString())
            e.printStackTrace()
        }
    }

    override fun onInterrupt() {
        hideOverlay()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            overlayJob?.cancel()
            hideOverlay()
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
            viewModelStore.clear()
        } catch (e: Exception) {
            Log.e("Error while destroying in MyAccessibilityService", e.toString())
        }
    }
}