package com.cpy3f2.gixor_mobile.model.entity

data class ResultData<T>(
    //响应码
    val code :Int,
    // 响应消息
    val msg :String,
    //返回的数据
    val data: T) {
    companion object {
        const val CODE_SUCCESS = 0
    }
    fun httpData() :T{
        if(code==CODE_SUCCESS){
            return data
        }else{
            throw RuntimeException(msg)
        }
    }
}


