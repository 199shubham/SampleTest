package com.globalmed.corelib.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferenceRepositoryImplementation(val application: Application): SharedPreferenceRepository {
	private val preferences: SharedPreferences = application.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)
	override fun writeStringToPreference(key: String, value: String?) {
		value?.let {
			preferences.edit().putString(key, it).apply()
		}
	}

	override fun readStringFromPreference(key: String): String? {
		return preferences.getString(key, null)
	}

	override fun writeIntToPreference(key: String, value: Int) {
		preferences.edit().putInt(key, value).apply()
	}

	override fun readIntFromPreference(key: String): Int {
		return preferences.getInt(key, -1)
	}

	override fun writeBooleanToPreferences(key: String, value: Boolean) {
		preferences.edit().putBoolean(key, value).apply()
	}

	override fun readBooleanToPreferences(key: String): Boolean {
		return preferences.getBoolean(key, false)
	}

	override fun clearPreferences() {
		preferences.edit().clear().apply()
	}

	override fun clearPreference(key: String) {
		preferences.edit().remove(key).apply()
	}

	override fun providePreference(): SharedPreferences{
		return preferences
	}
}