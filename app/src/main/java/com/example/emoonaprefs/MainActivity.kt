package com.example.emoonaprefs

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.emoonaprefs2.EmoonaPrefs
import com.example.emoonaprefs2.EmoonaPrefs.set
import com.example.emoonaprefs2.EmoonaPrefs.get
import com.example.emoonaprefs2.PrefsKeys
import com.example.emoonaprefs2.guard
// https://medium.com/@anujguptawork/how-to-create-your-own-android-library-and-publish-it-750e0f7481bf
class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instanciate Shared preferences
        prefs = EmoonaPrefs.defaultPrefs(context = this)

        // ------- First method -----

        prefs[PrefsKeys.isFisrstLaunch]?.let { isFirstLaunch ->

            if (isFirstLaunch) {
                // Do something for the first launch
                // ...
                // Now change the value like this
                prefs[PrefsKeys.isFisrstLaunch] = false
            } else {
                // Now create user
                val user = User(name = "Mickael", age = 24)

                // Save the user
                prefs[PrefsKeys.user] = user


                // And retrieve it like that
                prefs[PrefsKeys.user]?.let { user ->
                    println(user.toString())
                }
            }
        }


        // ----- Second method with guard statement more beautiful

        val isFirstLaunch = prefs[PrefsKeys.isFisrstLaunch].guard {
            println("The key is null, you can add default value")
            return
        }

        if (isFirstLaunch) {
            // Do something for the first launch
            // ...
            // Now change the value like this
            prefs[PrefsKeys.isFisrstLaunch] = false
        } else {
            // Now create user
            val user = User(name = "Mickael", age = 24)

            // Save the user
            prefs[PrefsKeys.user] = user

            // And retrieve it like that
            val retrievedUser = prefs[PrefsKeys.user].guard { return }
            println(retrievedUser.toString())
        }
    }
}

class User(val name: String, val age: Int)