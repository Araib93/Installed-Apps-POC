package com.poc.installedapps.base

import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    fun showToast(message: String? = null, exception: Exception? = null) {
        val toastMessage = message ?: run {
            if (exception == null) {
                "Show toast called with no message or exception"
            } else {
                exception.localizedMessage ?: exception.message ?: "Exception occurred"
            }
        }
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }
}