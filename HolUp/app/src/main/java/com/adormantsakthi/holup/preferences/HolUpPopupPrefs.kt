package com.adormantsakthi.holup.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Manages the persistent storage of target package names for the application.
 * Uses SharedPreferences for lightweight storage with automatic persistence.
 */
class HolUpPopupPrefs(context: Context) {

    // Get a reference to SharedPreferences specifically for package storage
    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }

    // Get Interruption Text, if not return default
    fun getInterruptionText(): String {
        return preferences.getString(INTERRUPTION_TEXT, "HolUp! You have these remaining tasks!") ?: "HolUp! You have these remaining tasks!"

    }

    // Edit the Interruption Text
    fun editInterruptionText(text: String){
        preferences.edit {
            putString(INTERRUPTION_TEXT, text)

        }
    }

    // Get Interruption Duration, if not return default
    fun getInterruptionDurationIndex(): Int {
        return preferences.getInt(INTERRUPTION_DURATION, 1)
    }

    // Edit Interruption Duration
    fun editInterruptionDuration(index: Int){
        preferences.edit {
            putInt(INTERRUPTION_DURATION, index)
        }
    }

    // Get Delay Btw App Switch Index
    fun getDelayBtwAppSwitchIndex(): Int {
        return preferences.getInt(DELAY_BTW_APP_SWITCH, 0)
    }

    // Edit Delay Btw App Switch Index
    fun editDelayBtwAppSwitchIndex(index: Int){
        preferences.edit {
            putInt(DELAY_BTW_APP_SWITCH, index)
        }
    }

    // Get Delay Btw Reinterruption Index
    fun getDelayBtwReinterruptionIndex(): Int {
        return preferences.getInt(REINTERRUPTION_DURATION, 0)
    }

    // Edit Delay Btw App Switch Index
    fun editDelayBtwReinterruptionIndex(index: Int){
        preferences.edit {
            putInt(REINTERRUPTION_DURATION, index)
        }
    }

    companion object {
        private const val PREFS_NAME = "HolUpPopupPrefs"
        private const val INTERRUPTION_TEXT = "interruption_text"
        private const val INTERRUPTION_DURATION = "interruption_duration"
        private const val DELAY_BTW_APP_SWITCH = "delay_btw_app_switch"
        private const val REINTERRUPTION_DURATION = "reinterruption_duration"
    }
}