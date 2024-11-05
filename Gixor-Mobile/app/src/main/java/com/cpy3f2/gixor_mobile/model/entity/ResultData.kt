package com.cpy3f2.gixor_mobile.model.entity

data class ResultData<T>(
    // 响应码
    val code: Int,
    // 响应消息
    val msg: String,
    // 返回的数据，可为空
    val data: T? = null  // 将 data 设为可空类型，并提供默认值 null
) {
    companion object {
        const val CODE_SUCCESS = 200
    }
    
    fun httpData(): T? {  // 修改返回类型为可空
        if (code == CODE_SUCCESS) {
            return data
        } else {
            throw RuntimeException(msg)
        }
    }
}


