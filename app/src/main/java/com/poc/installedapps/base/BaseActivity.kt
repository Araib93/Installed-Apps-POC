package com.poc.installedapps.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseActivity : AppCompatActivity() {
    fun replaceWithFragment(@IdRes id: Int, fragment: Fragment) {
        val tag = fragment::class.java.name
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            supportFragmentManager.beginTransaction()
                .replace(id, fragment, tag)
                .commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onPostCreate()
    }

    @CallSuper
    open fun onPostCreate() {
        // This is where you have permissions so do what you feel like doing
    }
}