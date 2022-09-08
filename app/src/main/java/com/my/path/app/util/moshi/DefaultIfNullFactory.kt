package com.my.path.app.util.moshi

import com.squareup.moshi.*
import java.lang.reflect.Type

/**
 * @author GuangNian
 * @description:
 * @date : 2022/6/2 3:03 下午
 */
class DefaultIfNullFactory : JsonAdapter.Factory {
    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {

        if (!annotations.isEmpty()) return null

        val delegate = moshi.nextAdapter<Any>(this, type, annotations)
        if (type === Boolean::class.javaPrimitiveType) return delegate
        if (type === Byte::class.javaPrimitiveType) return delegate
        if (type === Char::class.javaPrimitiveType) return delegate
        if (type === Double::class.javaPrimitiveType) return delegate
        if (type === Float::class.javaPrimitiveType) return delegate
        if (type === Int::class.javaPrimitiveType) return delegate
        if (type === Long::class.javaPrimitiveType) return delegate
        if (type === Short::class.javaPrimitiveType) return delegate
        if (type === Boolean::class.java) return delegate
        if (type === Byte::class.java) return delegate
        if (type === Char::class.java) return delegate
        if (type === Double::class.java) return delegate
        if (type === Float::class.java) return delegate
        if (type === Int::class.java) return delegate
        if (type === Long::class.java) return delegate
        if (type === Short::class.java) return delegate
        if (type === String::class.java) return delegate

        return object : JsonAdapter<Any>() {

            override fun fromJson(reader: JsonReader): Any? {

                try {
                    return if (reader.peek() == JsonReader.Token.BEGIN_OBJECT) {
                        delegate.fromJson(JsonReaderSkipNullValuesWrapper(reader))
                    } else {
                        delegate.fromJson(reader)
                    }
                }catch (e:Exception){}

                return null
            }

            override fun toJson(writer: JsonWriter, value: Any?) {
                return delegate.toJson(writer, value)
            }
        }
    }
}