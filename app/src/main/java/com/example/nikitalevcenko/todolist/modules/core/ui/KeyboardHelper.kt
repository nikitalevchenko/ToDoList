package com.example.nikitalevcenko.todolist.modules.core.ui

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

class KeyboardHelper(private val activity: Activity) {
    fun hideKeyboard() {
        val currentFocus = activity.currentFocus

        currentFocus?.let {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}