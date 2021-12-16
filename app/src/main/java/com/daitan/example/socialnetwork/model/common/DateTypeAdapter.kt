package com.daitan.example.socialnetwork.model.common

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.util.*

class DateTypeAdapter: TypeAdapter<Date>() {

    override fun write(writer: JsonWriter?, value: Date?) {
        if (value == null) {
            writer?.nullValue();
            return;
        }
        writer?.value(value.time)
    }

    override fun read(reader: JsonReader?): Date? {
        if (reader?.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }
        val long = reader?.nextLong()
        return long?.let {
            Date(long)
        }
    }
}