package com.cpy3f2.gixor_mobile.model.converter

import android.annotation.SuppressLint
import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    companion object {
        private val API_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    }

    @SuppressLint("NewApi")
    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.format(API_FORMATTER))
    }

    @SuppressLint("NewApi")
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime? {
        return try {
            json?.asString?.let {
                LocalDateTime.parse(it, API_FORMATTER)
            }
        } catch (e: Exception) {
            null
        }
    }
} 