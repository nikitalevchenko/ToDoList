package com.example.nikitalevcenko.todolist.modules.core.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.addTextChangedListener(onTextChanged: (text: String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(text?.toString() ?: "")
        }
    })
}