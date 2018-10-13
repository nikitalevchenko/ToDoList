package com.example.nikitalevcenko.todolist.modules.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.arch.persistence.room.Room
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.nikitalevcenko.todolist.R
import com.example.nikitalevcenko.todolist.db.DB
import com.example.nikitalevcenko.todolist.db.entity.Priority
import com.example.nikitalevcenko.todolist.modules.core.ui.observeOnMainThread
import com.example.nikitalevcenko.todolist.modules.task.presentation.TaskPresenter
import com.example.nikitalevcenko.todolist.modules.task.ui.TaskActivity
import com.example.nikitalevcenko.todolist.repo.tasks.Task
import io.reactivex.Completable


private const val ACTION_ON_CLICK = "com.example.nikitalevcenko.todolist.itemonclick"

private const val TASK_ID = "TASK_ID"
private const val TYPE = "TYPE"

private const val TYPE_ON_CLICK = 1
private const val TYPE_ON_DELETE = 2

class TasksWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        appWidgetIds.forEach { id ->
            appWidgetManager.updateAppWidget(id, RemoteViews(context.packageName, R.layout.widget).apply {

                setRemoteAdapter(R.id.widgetListView, Intent(context, TasksRemoteViewsService::class.java)
                        .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id))


                setPendingIntentTemplate(R.id.widgetListView, PendingIntent.getBroadcast(context,
                        1,
                        Intent(context, TasksWidget::class.java).setAction(ACTION_ON_CLICK),
                        PendingIntent.FLAG_CANCEL_CURRENT))

                setOnClickPendingIntent(R.id.widgetAddTaskButton, PendingIntent.getActivity(context,
                        2,
                        Intent(context, TaskActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra(TaskActivity.CONFIGURATOR, TaskPresenter.Configurator.createTask()),
                        PendingIntent.FLAG_CANCEL_CURRENT))

            })
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val type = intent.getIntExtra(TYPE, TYPE_ON_CLICK)

        when {
            action == AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                val mgr = AppWidgetManager.getInstance(context)
                val cn = ComponentName(context, TasksWidget::class.java)
                mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widgetListView)

                mgr.getAppWidgetIds(cn).forEach { id ->
                    mgr.updateAppWidget(id, RemoteViews(context.packageName, R.layout.widget).apply {
                        setRemoteAdapter(R.id.widgetListView, Intent(context, TasksRemoteViewsService::class.java)
                                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id))

                    })
                }

//                val rv = RemoteViews(context.packageName, R.layout.widget)
            }
            action == ACTION_ON_CLICK && type == TYPE_ON_DELETE -> {
                Completable.fromAction {
                    val db = DB.buildDB(context)
                    db.tasksDao.delete(intent.getLongExtra(TASK_ID, 0))
                    db.close()
                }.observeOnMainThread().subscribe {
                    context.sendBroadcast(Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
                        component = ComponentName(context, TasksWidget::class.java)
                    })
                }
            }

            action == ACTION_ON_CLICK && type == TYPE_ON_CLICK -> {
                context.startActivity(Intent(context, TaskActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .putExtra(TaskActivity.CONFIGURATOR, TaskPresenter.Configurator.editTask(intent.getLongExtra(TASK_ID, 0))))

            }
        }
        super.onReceive(context, intent)
    }
}

class TasksWidgetRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {


    private val db = DB.buildDB(context)

    private var tasks: List<Task> = emptyList()

    override fun onCreate() {}

    override fun getLoadingView() = null

    override fun getItemId(position: Int) = tasks[position].id

    override fun onDataSetChanged() {
        tasks = db.tasksDao.readAll().blockingFirst()
    }

    override fun hasStableIds() = true

    override fun getViewAt(position: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_item).apply {
            val task = tasks[position]

            when (task.priority) {
                Priority.HIGH -> {
                    setTextViewCompoundDrawables(R.id.titleTextView, R.drawable.point_high_proirity, 0, 0, 0)
//                    setInt(R.id.closeButton, "setBackgroundResource", R.drawable.background_high_priority_ripple)
//                    setInt(R.id.titleTextView, "setBackgroundResource", R.drawable.background_high_priority_ripple)


                }
                Priority.NORMAL -> {
                    setTextViewCompoundDrawables(R.id.titleTextView, R.drawable.point_normal_proirity, 0, 0, 0)
//                    setInt(R.id.closeButton, "setBackgroundResource", R.drawable.background_normal_priority_ripple)
//                    setInt(R.id.titleTextView, "setBackgroundResource", R.drawable.background_normal_priority_ripple)

                }
                Priority.LOW -> {
                    setTextViewCompoundDrawables(R.id.titleTextView, R.drawable.point_low_proirity, 0, 0, 0)
//                    setInt(R.id.closeButton, "setBackgroundResource", R.drawable.background_start_range_ripple)
//                    setInt(R.id.titleTextView, "setBackgroundResource", R.drawable.background_start_range_ripple)

                }
            }

            when (position) {
                0 -> {
                    setInt(R.id.widgetItemRootLinearLayout, "setBackgroundResource", R.drawable.background_widget_deadline_expired)
                    setInt(R.id.closeButton, "setBackgroundResource", R.drawable.background_deadline_expired_ripple)
                    setInt(R.id.titleTextView, "setBackgroundResource", R.drawable.background_deadline_expired_ripple)
                }
                1 -> {
                    setInt(R.id.widgetItemRootLinearLayout, "setBackgroundResource", R.drawable.background_widget_deadline)
                    setInt(R.id.closeButton, "setBackgroundResource", R.drawable.background_deadline_ripple)
                    setInt(R.id.titleTextView, "setBackgroundResource", R.drawable.background_deadline_ripple)
                }
                2 -> {
                    setInt(R.id.widgetItemRootLinearLayout, "setBackgroundResource", R.drawable.background_widget_before_deadline)
                    setInt(R.id.closeButton, "setBackgroundResource", R.drawable.background_before_deadline_ripple)
                    setInt(R.id.titleTextView, "setBackgroundResource", R.drawable.background_before_deadline_ripple)
                }
                3 -> {
                    setInt(R.id.widgetItemRootLinearLayout, "setBackgroundResource", R.drawable.background_widget_end_range)
                    setInt(R.id.closeButton, "setBackgroundResource", R.drawable.background_end_range_ripple)
                    setInt(R.id.titleTextView, "setBackgroundResource", R.drawable.background_end_range_ripple)
                }
                4 -> {
                    setInt(R.id.widgetItemRootLinearLayout, "setBackgroundResource", R.drawable.background_widget_start_range)
                    setInt(R.id.closeButton, "setBackgroundResource", R.drawable.background_start_range_ripple)
                    setInt(R.id.titleTextView, "setBackgroundResource", R.drawable.background_start_range_ripple)
                }
                else -> {
                    setInt(R.id.widgetItemRootLinearLayout, "setBackgroundResource", R.drawable.background_widget_without_deadline)
                    setInt(R.id.closeButton, "setBackgroundResource", R.drawable.background_without_deadline_ripple)
                    setInt(R.id.titleTextView, "setBackgroundResource", R.drawable.background_without_deadline_ripple)
                }
            }

            setTextViewText(R.id.titleTextView, task.name)

            setOnClickFillInIntent(R.id.closeButton, Intent().putExtra(TASK_ID, tasks[position].id).putExtra(TYPE, TYPE_ON_DELETE))
            setOnClickFillInIntent(R.id.titleTextView, Intent().putExtra(TASK_ID, tasks[position].id).putExtra(TYPE, TYPE_ON_CLICK))
        }
    }

    override fun getCount() = tasks.size

    override fun getViewTypeCount() = 1

    override fun onDestroy() {
        db.close()
    }
}


class TasksRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?) = TasksWidgetRemoteViewsFactory(this.applicationContext)
}