package com.adormantsakthi.holup

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.collection.intListOf
import androidx.collection.longListOf
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
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
import com.adormantsakthi.holup.functions.statistics.NumOfTimesLimitedAppsAccessedStorage
import com.adormantsakthi.holup.storage.HolUpPopupPrefs
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
import java.util.concurrent.atomic.AtomicBoolean


var lastExecutionTime: MutableLongState = mutableLongStateOf(0) // Timestamp of the last function execution

val overlayClosed = AtomicBoolean(true)

class OverlayStateManager (context: Context) {
    private val setOfDelayBtwAppSwitch = intListOf(60000, 120000, 300000, 600000)
    private val delayBtwAppSwitch =  mutableIntStateOf(HolUpPopupPrefs(context).getDelayBtwAppSwitchIndex())

    // Tracks whether the overlay should be visible
    private val _isOverlayVisible = MutableStateFlow(false)
    val isOverlayVisible: StateFlow<Boolean> = _isOverlayVisible

    // Initialize the package manager
    private val packageManager = LimitedAppsStorage(context)

    // Method to access the package manager for configuration
    fun getPackageManager(): LimitedAppsStorage = packageManager

    private val lock = Object() // To ensure thread safety if needed

    // Called when a new app is detected
    fun onAppOpened(packageName: String) {
        cancelTimer()

        if (overlayClosed.get()){
            if (getPackageManager().containsPackage(packageName) && !isOverlayVisible.value) {

                synchronized(lock) {
                    val currentTime = System.currentTimeMillis()

                    if (currentTime - lastExecutionTime.longValue <= setOfDelayBtwAppSwitch[delayBtwAppSwitch.intValue]) {
                        val remainingTime = (setOfDelayBtwAppSwitch[delayBtwAppSwitch.intValue] - (currentTime - lastExecutionTime.longValue)) / 1000
                        Log.d("Overlay Timer", "Overlay on cooldown for $remainingTime more seconds")
                        return
                    }
                }

                _isOverlayVisible.value = true
                overlayClosed.set(false)
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
    val holUpPopupPrefs = HolUpPopupPrefs(context)

    /**
     * Starts or resets the timer.
     */
    fun startOrResetTimer() {
        val antiDoomscrollTimeList = longListOf(60000, 120000, 300000, 600000)
        val antiDoomscrollTimeIndex = holUpPopupPrefs.getDelayBtwReinterruptionIndex()
        // Cancel any existing timer
        timerRunnable?.let { handler.removeCallbacks(it) }

        // Create a new runnable to execute after the timeout
        timerRunnable = Runnable {
            Log.d("Anti-Doomscroll", "Anti_Doomscroll activated")
            _isOverlayVisible.value = true
            overlayClosed.set(false)
        }

        try {
            Log.d("Timer For Anti-Doomscroll", antiDoomscrollTimeList[antiDoomscrollTimeIndex].toString())
            // Post the runnable with the specified delay
            handler.postDelayed(timerRunnable!!, antiDoomscrollTimeList[antiDoomscrollTimeIndex])
        } catch (e: Exception) {
            Log.e("Timer For Anti-Doomscroll", e.toString())
        }

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
    private var launcherPackageName: String? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        overlayStateManager = OverlayStateManager(this)
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

        fun getLauncherPackageName(context: Context): String? {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            val resolveInfo = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            return resolveInfo?.activityInfo?.packageName
        }

        launcherPackageName = getLauncherPackageName(context = this)
    }

    // Add these properties to track state
    private var overlayJob: Job? = null
    private lateinit var overlayStateManager: OverlayStateManager
    private var openedPackageName: String = ""
    private var previouslyOpenedPackageName: String = ""

    private var lastEventTime: Long = 0L // Tracks the last event timestamp
    private val debounceTimeMS = 500L // Adjust debounce time as needed

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return

            // Debounce logic: Ignore events that happen too quickly
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastEventTime < debounceTimeMS) {
                return
            }
            lastEventTime = currentTime

            if (packageName == openedPackageName || packageName == "com.android.systemui" || packageName == "com.adormantsakthi.holup") {
                return
            } else if (packageName == launcherPackageName) {
                // when in homescreen, clear the previously opened package name

                previouslyOpenedPackageName = ""
                openedPackageName = packageName
                Log.d("App Currently Open", packageName)
                Log.d("App Previously Open", previouslyOpenedPackageName)
                return
            } else if (packageName == previouslyOpenedPackageName) {
                previouslyOpenedPackageName = openedPackageName
                openedPackageName = packageName
                Log.d("App Currently Open", packageName)
                Log.d("App Previously Open", previouslyOpenedPackageName)
                return
            }

            CoroutineScope(Dispatchers.Default).launch {
                overlayStateManager.onAppOpened(packageName)
            }

            previouslyOpenedPackageName = openedPackageName
            openedPackageName = packageName

            Log.d("App Currently Open", packageName)
            Log.d("App Previously Open", previouslyOpenedPackageName)
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
                            NumOfTimesLimitedAppsAccessedStorage.appAccessed(context)
                            handler.postDelayed({
                                overlayClosed.set(true)
                            }, 100)

                            // starts AntiDoomscroll timer if app package name in Reinterruption Storage
                            CoroutineScope(Dispatchers.IO).launch {
                                if (ReInterruptionStorage(context).containsPackage(openedPackageName)) {
                                    Log.d("Opened Package Name", openedPackageName)
                                    overlayStateManager.startOrResetTimer()
                                }
                            }
                        },

                        onClose = {
                            lastExecutionTime.longValue = 0
                            closeCurrentApp()
                            val handler = android.os.Handler(Looper.getMainLooper())
                            handler.postDelayed({
                                overlayClosed.set(true)
                            }, 100)
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
            Handler(Looper.getMainLooper()).post {
                overlayView?.let { view ->
                    windowManager?.removeView(view)
                    overlayView = null
                    System.gc() // Request garbage collection to clean up native resources
                }
            }
        } catch (e: Exception) {
            Log.e("Error while hiding overlay", e.toString())
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