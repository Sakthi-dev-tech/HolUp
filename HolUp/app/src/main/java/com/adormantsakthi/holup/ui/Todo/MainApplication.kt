package com.adormantsakthi.holup.ui.Todo

import android.app.Activity
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.room.Room
import com.adormantsakthi.holup.functions.NotificationAlarmReceiver
import com.adormantsakthi.holup.functions.database.TodoDatabase
import com.adormantsakthi.holup.preferences.BillingManager

class MainApplication : Application() {

    // Make the BillingManager accessible throughout your app
    lateinit var billingManager: BillingManager

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Schedule the daily notification
        NotificationAlarmReceiver.scheduleDaily(this)

        // Initialize your database
        todoDatabase = Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            TodoDatabase.NAME
        ).build()

        try {
            // Initialize the BillingManager
            billingManager = BillingManager(applicationContext)
            billingManager.setupBillingClient()  // Ensure this is called to set up the BillingClient properly
            Log.d("Billing Manager Initialisation", "Success")
        } catch (e: Exception) {
            Log.e("Error while initialising Billing Manager", e.toString())
        }
    }

    companion object {
        lateinit var todoDatabase: TodoDatabase

        private lateinit var instance: MainApplication

        fun getInstance(): MainApplication {
            return instance
        }
    }

    override fun onTerminate() {
        super.onTerminate()

        billingManager.endBillingClientConnection()
    }
}
