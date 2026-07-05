package com.example.linkopner.logic

import android.content.Context
import android.content.SharedPreferences

class StatsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("linkhunter_stats", Context.MODE_PRIVATE)

    fun getScannedCount(): Int = prefs.getInt("scanned_count", 0)
    fun getSuspiciousCount(): Int = prefs.getInt("suspicious_count", 0)
    fun getNeutralizedCount(): Int = prefs.getInt("neutralized_count", 0)

    fun incrementScanned() {
        val current = getScannedCount()
        prefs.edit().putInt("scanned_count", current + 1).apply()
    }

    fun incrementSuspicious() {
        val current = getSuspiciousCount()
        prefs.edit().putInt("suspicious_count", current + 1).apply()
    }

    fun incrementNeutralized() {
        val current = getNeutralizedCount()
        prefs.edit().putInt("neutralized_count", current + 1).apply()
    }
}
