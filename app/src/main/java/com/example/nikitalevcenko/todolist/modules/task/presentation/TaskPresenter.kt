package com.example.nikitalevcenko.todolist.modules.task.presentation

import android.os.Parcel
import android.os.Parcelable
import com.arellomobile.mvp.InjectViewState
import com.example.nikitalevcenko.todolist.App
import com.example.nikitalevcenko.todolist.R
import com.example.nikitalevcenko.todolist.db.entity.Priority
import com.example.nikitalevcenko.todolist.modules.core.presentation.BasePresenter
import com.example.nikitalevcenko.todolist.modules.task.di.TaskModule
import com.example.nikitalevcenko.todolist.modules.task.interactor.TaskInteractor
import com.example.nikitalevcenko.todolist.repo.tasks.Task
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject

@InjectViewState
class TaskPresenter(private val configurator: Configurator) : BasePresenter<TaskView>() {

    @Inject
    lateinit var interactor: TaskInteractor

    private var name: String = ""
        set(value) {
            if (field != value) {
                field = value
                viewState.setEmptyNameError(false)
                viewState.setTaskName(value)
            }
        }

    private var priority = Priority.NORMAL
        set(value) {
            field = value
            viewState.setTaskPriority(value)
        }

    private var description: String = ""
        set(value) {
            if (field != value) {
                field = value
                viewState.setTaskDescription(value)
            }
        }

    private var showDate: Date = Date()
        set(value) {
            if (field != value) {
                field = value
                viewState.setShowDate(value)
            }
        }

    private var deadlineDate: Date? = null
        set(value) {
            if (field != value) {
                field = value
                viewState.setDeadlineDate(value)
            }
        }

    private var taskDisposable: Disposable? = null

    init {
        App.component.plus(TaskModule()).inject(this)
        viewState.setSubmitButtonTitle(if (configurator.taskId == 0L) R.string.add_task else R.string.save)
        if (configurator.taskId != 0L) {
            taskDisposable = interactor.getTask(configurator.taskId).subscribe { task ->
                name = task.name
                priority = task.priority
                description = task.description
                showDate = task.showDate
                deadlineDate = task.deadlineDate
            }
        } else {
            priority = Priority.NORMAL
            showDate = Date()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        taskDisposable?.dispose()
    }

    fun onNameChanged(name: String) {
        this.name = name
    }

    fun onLowPriorityButtonClick() {
        this.priority = Priority.LOW
    }

    fun onNormalPriorityButtonClick() {
        this.priority = Priority.NORMAL
    }

    fun onHighPriorityButtonClick() {
        this.priority = Priority.HIGH
    }

    fun onDescriptionChanged(description: String) {
        this.description = description
    }

    fun onSubmitButtonClick() {
        if (this.name.isEmpty()) {
            viewState.setEmptyNameError(true)
        } else {
            // TODO change isCompleted
            interactor.updateOrUpdateTask(Task(configurator.taskId, name, description, priority, showDate, deadlineDate)).subscribe()
            viewState.exit()
        }
    }

    fun onDeadlineDateButtonClick() {
        viewState.setDeadlineDatePickerVisibility(true, deadlineDate, showDate)
    }

    fun onShowDateButtonClick() {
        viewState.setShowDatePickerVisibility(true, showDate, deadlineDate)
    }

    fun onShowDateChanged(date: Date) {
        showDate = date
    }

    fun onDeadlineDateChanged(date: Date?) {
        deadlineDate = date
    }


    class Configurator(val taskId: Long) : Parcelable {
        constructor(parcel: Parcel) : this(parcel.readLong()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeLong(taskId)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Configurator> {

            fun createTask(): Configurator = Configurator(0)
            fun editTask(taskId: Long): Configurator = Configurator(taskId)
            fun notifyToUseWidget(): Configurator = Configurator(-1)

            override fun createFromParcel(parcel: Parcel): Configurator {
                return Configurator(parcel)
            }

            override fun newArray(size: Int): Array<Configurator?> {
                return arrayOfNulls(size)
            }
        }


    }
}