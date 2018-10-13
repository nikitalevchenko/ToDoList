package com.example.nikitalevcenko.todolist.modules.core.ui

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers

fun Completable.observeOnMainThread(): Completable = this.subscribeOn(io.reactivex.schedulers.Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.observeOnMainThread(): Flowable<T> = this.subscribeOn(io.reactivex.schedulers.Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())