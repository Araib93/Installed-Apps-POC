package com.poc.installedapps.model

import android.net.Uri

data class App(val `package`: String, val name: String, val imageUrl: Uri) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is App -> {
                other.`package` == this.`package`
            }
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return `package`.hashCode() + name.hashCode() + super.hashCode()
    }
}