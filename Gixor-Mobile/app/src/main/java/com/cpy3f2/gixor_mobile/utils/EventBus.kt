package com.cpy3f2.gixor_mobile.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object EventBus {
    private val followEventFlow = MutableSharedFlow<Unit>()

    suspend fun emitFollowEvent() {
        followEventFlow.emit(Unit)
    }

    fun onFollowEvent(): kotlinx.coroutines.flow.SharedFlow<Unit> = followEventFlow.asSharedFlow()
}