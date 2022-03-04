package com.poc.installedapps.ui

import android.content.Context
import android.content.Intent
import com.poc.installedapps.R
import com.poc.installedapps.base.BaseActivity

class MainActivity : BaseActivity() {
    companion object {
        const val TAG = "MainActivity"

        fun start(context: Context, flags: Array<Int> = arrayOf()) {
            val intent = Intent(context, MainActivity::class.java)
            flags.forEach { intent.addFlags(it) }
            context.startActivity(intent)
        }
    }

    override fun onPostCreate() {
        super.onPostCreate()
        setContentView(R.layout.activity_main)
        replaceWithFragment(R.id.container, MainFragment.newInstance())
    }
}