package com.example.nikitalevcenko.todolist.modules.core.presentation

import com.arellomobile.mvp.MvpPresenter
import com.example.nikitalevcenko.todolist.modules.core.view.BaseView

abstract class BasePresenter<V : BaseView> : MvpPresenter<V>() {
}