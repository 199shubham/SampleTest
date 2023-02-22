package com.globalmed.corelib.repository

import android.content.SharedPreferences

interface SharedPreferenceRepository {
	fun writeStringToPreference(key: String, value: String?)
	fun readStringFromPreference(key: String): String?
	fun writeIntToPreference(key: String, value: Int)
	fun readIntFromPreference(key: String): Int
	fun writeBooleanToPreferences(key: String, value:Boolean)
	fun readBooleanToPreferences(key: String): Boolean
	fun clearPreferences()
	fun clearPreference(key: String)
	fun providePreference(): SharedPreferences
}