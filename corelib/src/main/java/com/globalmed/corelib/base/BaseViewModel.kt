package com.globalmed.corelib.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import com.globalmed.corelib.BuildConfig
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.*

/**
 * @author rsharma
 * @since 26-04-2021
 * BaseViewModel which is a AndroidViewModel, that hold a BaseNavigator.
 */
abstract class BaseViewModel<out MarkerBaseNavigator>(application: Application): AndroidViewModel(application) {
	abstract val navigator: MarkerBaseNavigator
	lateinit var messages: JSONObject
	init {
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			loadErrors("error_codes_${application.resources.configuration.locales.get(0).language}.json")
		}else loadErrors("error_codes_${application.resources.configuration.locale.language}.json")*/
		loadErrors("error_codes_en.json")
	}
	private fun loadErrors(fileName: String){
		getApplication<Application>().assets.open(fileName).let {
			val size = it.available()
			val data = ByteArray(size)
			it.read(data)
			it.close()
			messages = JSONObject(String(data, Charset.forName("UTF-8")))
		}
	}

	fun getMessage(screenName: String, code: String): String{
		return try {
			messages.getJSONObject(screenName).let { obj->
				if(obj.has(code)){
					obj.getString(code)
				}else{
					code
				}
			}
		}catch (ex: Exception){
			code
		}
	}
}