package com.example.nikitalevcenko.todolist.modules.core.ui

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.WindowManager

val Activity.ANIMATION_DURATION get() = this.resources.getInteger(android.R.integer.config_shortAnimTime)

fun Activity.getStatusBarHeight() = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
fun Activity.getNavigationBarHeight() = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"))

fun Activity.getScreenHeight() = DisplayMetrics().apply {
    (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(this)
}.widthPixels

fun Activity.setStatusBarColor(@ColorRes colorRes: Int, onAnimationEnd: (() -> Unit)? = null) {
    val color = ContextCompat.getColor(this, colorRes)
    window?.statusBarColor?.let { statusBarColor ->
        if (statusBarColor != color) {
            ValueAnimator.ofObject(ArgbEvaluator(),
                    statusBarColor,
                    color).apply {
                addUpdateListener {
                    window?.statusBarColor = it.animatedValue as Int
                }

                onAnimationEnd?.let {
                    addListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) {

                        }

                        override fun onAnimationEnd(p0: Animator?) {
                            it()
                        }

                        override fun onAnimationCancel(p0: Animator?) {

                        }

                        override fun onAnimationStart(p0: Animator?) {

                        }

                    })

                }



                duration = ANIMATION_DURATION.toLong()

            }.start()
        }
    }
}