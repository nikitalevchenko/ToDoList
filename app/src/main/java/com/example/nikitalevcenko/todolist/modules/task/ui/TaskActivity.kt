package com.example.nikitalevcenko.todolist.modules.task.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.text.format.DateUtils
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.nikitalevcenko.todolist.R
import com.example.nikitalevcenko.todolist.db.entity.Priority
import com.example.nikitalevcenko.todolist.modules.core.ui.*
import com.example.nikitalevcenko.todolist.modules.task.presentation.TaskPresenter
import com.example.nikitalevcenko.todolist.modules.task.presentation.TaskView
import kotlinx.android.synthetic.main.activity_task.*
import java.util.*

class TaskActivity : BaseActivity(), TaskView {

    companion object {
        const val CONFIGURATOR = "CONFIGURATOR"
    }

    @InjectPresenter
    lateinit var presenter: TaskPresenter

    private val dateFormatter = DateFormatter()

    @ProvidePresenter
    fun providePresenter(): TaskPresenter {
        return TaskPresenter(intent?.extras?.getParcelable(CONFIGURATOR)
                ?: TaskPresenter.Configurator.createTask())
    }

    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(taskBottomSheet) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras.let {
            if (it == null || !it.containsKey(CONFIGURATOR)) {
                Toast.makeText(this, R.string.please_use_app_widget, Toast.LENGTH_SHORT).show()
                finish()
                return
            }
        }

        setContentView(R.layout.activity_task)

        taskNameEditText.addTextChangedListener { presenter.onNameChanged(it) }
        taskNameEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT
                    || event.action == KeyEvent.ACTION_UP
                    && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                currentFocus?.let {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                }
            }
            true
        }

        taskDescriptionEditText.addTextChangedListener {
            presenter.onDescriptionChanged(it)
        }
//        taskDescriptionEditText.setOnEditorActionListener { _, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_DONE
//                    || event.action == KeyEvent.ACTION_UP
//                    && event.keyCode == KeyEvent.KEYCODE_ENTER) {
//                currentFocus?.let {
//                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.hideSoftInputFromWindow(it.windowToken, 0)
//                }
//            }
//            true
//        }

//        taskDescriptionEditText.setOnEditorActionListener { _, actionId, event ->
//
//            if (actionId == EditorInfo.IME_ACTION_DONE
//                    || event.action == KeyEvent.ACTION_UP
//                    && event.keyCode == KeyEvent.KEYCODE_ENTER) {
//                currentFocus?.let {
//                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.hideSoftInputFromWindow(it.windowToken, 0)
//                }
//
//            }
//            true
//        }

        selectDeadlineDateButton.setOnClickListener { presenter.onDeadlineDateButtonClick() }
        selectShowDateButton.setOnClickListener { presenter.onShowDateButtonClick() }

        lowPriorityButton.setOnClickListener { presenter.onLowPriorityButtonClick() }
        normalPriorityButton.setOnClickListener { presenter.onNormalPriorityButtonClick() }
        highPriorityButton.setOnClickListener { presenter.onHighPriorityButtonClick() }

        submitButton.setOnClickListener { presenter.onSubmitButtonClick() }


        bottomSheetBehavior.peekHeight = taskBottomSheet.measuredHeight
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    finish()
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }

        })

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        taskBottomSheet.startAnimation(TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                1f,
                Animation.RELATIVE_TO_SELF,
                0f).apply {
            duration = ANIMATION_DURATION.toLong()
        })
    }

    // TaskView
    override fun setSubmitButtonTitle(titleRes: Int) {
        submitButton.setText(titleRes)
    }

    override fun setTaskName(name: String) {
        if (taskNameEditText.text.toString() != name) {
            taskNameEditText.setText(name)
            if (taskNameEditText.hasFocus()) {
                taskNameEditText.setSelection(name.length)
            }
        }
    }

    override fun setTaskPriority(priority: Priority) {
        lowPriorityButton.isSelected = priority == Priority.LOW
        normalPriorityButton.isSelected = priority == Priority.NORMAL
        highPriorityButton.isSelected = priority == Priority.HIGH

        submitButton.setBackground(backgroundRes = when (priority) {
            Priority.HIGH -> R.drawable.background_high_priority_button
            Priority.NORMAL -> R.drawable.background_normal_priority_button
            Priority.LOW -> R.drawable.background_low_priority_button
        })
    }

    override fun setTaskDescription(description: String) {
        if (taskDescriptionEditText.text.toString() != description) {
            taskDescriptionEditText.setText(description)
        }
    }

    override fun setDeadlineDate(deadline: Date?) {
        selectDeadlineDateButton.text = if (deadline == null) {
            getString(R.string.without_deadline)
        } else {
            getString(R.string.deadline_date, dateFormatter.format(deadline))
        }
    }

    override fun setShowDate(showDate: Date) {
        selectShowDateButton.text = when {
            DateUtils.isToday(showDate.time) -> getString(R.string.show_now)
            else -> getString(R.string.select_show_date, dateFormatter.format(showDate))
        }
    }

    override fun setEmptyNameError(hasError: Boolean) {
        taskNameTextInputLayout.error = if (hasError) getString(R.string.empty_name) else null
//
//        if (hasError) {
//            taskNestedScrollView.smoothScrollTo(0, taskNameTextInputLayout.top)
//        }
    }

    override fun setShowDatePickerVisibility(isVisible: Boolean, startDate: Date?, maxDate: Date?, onDissmissListener: () -> Unit) {
        if (isVisible) {
            if (startDate == null) return

            DatePickerDialog(this, R.style.ShowDateDatePicker, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                presenter.onShowDateChanged(Date(year - 1900, month, dayOfMonth))
            }, startDate.year + 1900, startDate.month, startDate.date).apply {
                setOnDismissListener { onDissmissListener() }
                datePicker.minDate = Date().time
                maxDate?.let { datePicker.maxDate = it.time }
            }.show()
        }
    }

    override fun setDeadlineDatePickerVisibility(isVisible: Boolean, startDate: Date?, minDate: Date?, onDissmissListener: () -> Unit) {
        if (isVisible) {

            val date = startDate ?: minDate ?: Date()

            DatePickerDialog(this, R.style.DeadlineDatePicker, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                presenter.onDeadlineDateChanged(Date(year - 1900, month, dayOfMonth))
            }, date.year + 1900, date.month, date.date).apply {
                setOnDismissListener { onDissmissListener() }
                setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.without_deadline)) { _, _ ->
                    presenter.onDeadlineDateChanged(null)
                    dismiss()
                }
                datePicker.minDate = minDate!!.time
            }.show()
        }
    }

    override fun onBackPressed() {
        exit()
    }

    override fun exit() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
}