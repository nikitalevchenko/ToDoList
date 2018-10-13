package com.example.nikitalevcenko.todolist.modules.task.presentation

import android.support.annotation.StringRes
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.nikitalevcenko.todolist.db.entity.Priority
import com.example.nikitalevcenko.todolist.modules.core.view.BaseView
import java.util.*

@StateStrategyType(AddToEndSingleStrategy::class)
interface TaskView : BaseView {
    fun setTaskName(name: String)
    fun setTaskPriority(priority: Priority)
    fun setTaskDescription(description: String)
    fun setShowDate(showDate: Date)
    fun setDeadlineDate(deadline: Date?)
    fun setEmptyNameError(hasError: Boolean)
    fun exit()
    fun setSubmitButtonTitle(@StringRes titleRes: Int)
    fun setDeadlineDatePickerVisibility(isVisible: Boolean, startDate: Date? = null, minDate: Date? = null, onDissmissListener: () -> Unit = { setDeadlineDatePickerVisibility(false) })
    fun setShowDatePickerVisibility(isVisible: Boolean, startDate: Date? = null, maxDate: Date? = null, onDissmissListener: () -> Unit = { setShowDatePickerVisibility(false) })
}