package com.example.emoonaprefs

import com.example.emoonaprefs2.DefaultsKey
import com.example.emoonaprefs2.PrefsKeys

val PrefsKeys.Companion.user get() = DefaultsKey<User>(key = "ep_user")
val PrefsKeys.Companion.isFisrstLaunch get() = DefaultsKey<Boolean>(key = "ep_isFirstLaunch", defaultValue = true)