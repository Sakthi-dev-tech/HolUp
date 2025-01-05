package com.adormantsakthi.holup

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        // Initialization when the service is connected
        Log.i("com.adormantsakthi.holup.MyAccessibilityService", "Accessibility Service Connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // Log all event types
        Log.d("MyAccessibilityService", "Event Type: ${event.eventType}")
        Log.d("MyAccessibilityService", "Package Name: ${event.packageName}")

        // Listen specifically for window state changes
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName.toString()
            Log.d("MyAccessibilityService", "Current app in foreground: $packageName")

        }
    }

    override fun onInterrupt() {
        // Handle service interruption if needed
        Log.d("com.adormantsakthi.holup.MyAccessibilityService", "Accessibility Service Interrupted")
    }
}