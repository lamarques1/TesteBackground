package com.example.testebackground

import android.content.Context

class SharedPref {
    fun setStartTime(context: Context, startTime: Long){
        val settings = context.getSharedPreferences(context.getString(R.string.shared_pref_file_key), Context.MODE_PRIVATE)
        val editor = settings.edit()

        editor.putString("startTime", startTime.toString())
        editor.apply()
    }

    /**
     * Get music information stored on Shared Preferences
     */
    fun getStartTime(context: Context): Long {
        val settings = context.getSharedPreferences(context.getString(R.string.shared_pref_file_key), 0)

        val startTime = settings.getString("startTime", "0")

        return startTime!!.toLong()
    }
}