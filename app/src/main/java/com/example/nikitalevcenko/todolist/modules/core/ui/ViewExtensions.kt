package com.example.nikitalevcenko.todolist.modules.core.ui

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.View

fun View.setBackground(@DrawableRes backgroundRes: Int) {
    background = TransitionDrawable(arrayOf(background, ContextCompat.getDrawable(context!!, backgroundRes))).apply {
        startTransition(resources.getInteger(android.R.integer.config_shortAnimTime))
    }
}

fun View.setBackgroundColor(@ColorRes backgroundColorRes: Int) {
    background = TransitionDrawable(arrayOf(background, ColorDrawable(ContextCompat.getColor(context!!, backgroundColorRes)))).apply {
        startTransition(resources.getInteger(android.R.integer.config_shortAnimTime))
    }
}