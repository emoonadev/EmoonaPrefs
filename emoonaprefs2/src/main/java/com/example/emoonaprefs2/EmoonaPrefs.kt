package com.example.emoonaprefs2

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import java.util.*

// MARK: - Models

class DefaultsKey<T>(val key: String, val defaultValue: T? = null) : DefaultsKeys()
open class DefaultsKeys

class PrefsKeys {
    companion object
}

class Defaults {
    companion object
}

object EmoonaPrefs {

    fun defaultPrefs(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    fun customPrefs(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int     -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float   -> edit { it.putFloat(key, value) }
            is Long    -> edit { it.putLong(key, value) }
            is Date -> edit { it.putLong(key, value.time) }
            else       -> throw UnsupportedOperationException("The shared type not yet implemented Preference helper")
        }
    }

    inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class  -> getString(key, defaultValue as? String) as T?
            Int::class     -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class   -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class    -> getLong(key, defaultValue as? Long ?: -1) as T?
            else           -> return null
        }

    }


    // MARK: - Gson support

    inline operator fun <reified T : Any> SharedPreferences.set(key: DefaultsKey<T>, value: T) {
        val jsonValue = Gson().toJson(value)
        this[key.key] = jsonValue
    }

    inline operator fun <reified T : Any> SharedPreferences.get(key: DefaultsKey<T>): T? {
        val json = this[key.key, ""]

        if (json?.isEmpty() != false) {
            if (key.defaultValue != null) {
                return key.defaultValue
            }

            return null
        }

        val obj = Gson().fromJson<T>(json, T::class.java)
        return (obj as T)
    }

}


// MARK: - Extension guard statement like swift

inline fun <reified T> T?.guard(call: () -> Unit): T {
    return if (this is T) this
    else {
        call()
        null!!
    }
}