package com.adormantsakthi.holup.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.adormantsakthi.holup.storage.LimitedAppsStorage.Companion

/**
 * Manages the persistent storage of target package names for the application.
 * Uses SharedPreferences for lightweight storage with automatic persistence.
 */
class ReInterruptionStorage(context: Context) {

    // get the list of limited apps
    val listOfLimitedApps = LimitedAppsStorage(context).getTargetPackages()

    // Get a reference to SharedPreferences specifically for package storage
    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }

    /**
     * Retrieves the current set of stored package names.
     * If no packages are stored yet, initializes with default packages.
     */
    fun getTargetPackages(): Set<String> {
        // If no packages exist yet, create an empty set
        if (!preferences.contains(KEY_PACKAGES)) {
            setTargetPackages(emptySet())
        }

        // If packages exist, check if they are also in the list of limited apps
        // If they are, let them be and if they are not, remove them
        val currentPackages = preferences.getStringSet(KEY_PACKAGES, emptySet()) ?: emptySet()
        val updatedPackages = currentPackages.filter { it in listOfLimitedApps }.toSet()
        setTargetPackages(updatedPackages)

        return preferences.getStringSet(KEY_PACKAGES, emptySet()) ?: emptySet()
    }

    /**
     * Stores a new set of package names, completely replacing the existing set.
     */
    private fun setTargetPackages(packages: Set<String>) {
        preferences.edit {
            putStringSet(KEY_PACKAGES, packages)
        }
    }

    fun clearData() {
        setTargetPackages(emptySet())
    }

    /**
     * Adds a single package name to the existing set.
     * Returns true if the package was successfully added (wasn't already present).
     */
    fun addPackage(packageName: String): Boolean {
        val currentPackages = getTargetPackages().toMutableSet()
        val wasAdded = currentPackages.add(packageName)
        if (wasAdded) {
            setTargetPackages(currentPackages)
        }
        return wasAdded
    }

    /**
     * Removes a package name from the stored set.
     * Returns true if the package was successfully removed (was present).
     */
    fun removePackage(packageName: String): Boolean {
        val currentPackages = getTargetPackages().toMutableSet()
        val wasRemoved = currentPackages.remove(packageName)
        if (wasRemoved) {
            setTargetPackages(currentPackages)
        }
        return wasRemoved
    }

    /**
     * Checks if a specific package is in the target list.
     */
    fun containsPackage(packageName: String): Boolean {
        return getTargetPackages().contains(packageName)
    }

    companion object {
        private const val PREFS_NAME = "reinterruption_apps"
        private const val KEY_PACKAGES = "reinterruption_packages"
    }
}