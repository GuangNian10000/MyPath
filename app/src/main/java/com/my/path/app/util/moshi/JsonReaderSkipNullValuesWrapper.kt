package com.squareup.moshi
import okio.BufferedSource
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author GuangNian
 * @description:
 * @date : 2022/6/2 3:06 下午
 */
class JsonReaderSkipNullValuesWrapper(
    private val wrapped: JsonReader
) : JsonReader() {

    private var ignoreSkipName = AtomicBoolean(false)
    private var ignoreSkipValue = AtomicBoolean(false)

    override fun close() {
        wrapped.close()
    }

    override fun beginArray() {
        wrapped.beginArray()
    }

    override fun endArray() {
        wrapped.endArray()
    }

    override fun beginObject() {
        wrapped.beginObject()
    }

    override fun endObject() {
        wrapped.endObject()
        ignoreSkipName.compareAndSet(true, false)
        ignoreSkipValue.compareAndSet(true, false)
    }

    override fun hasNext(): Boolean {
        return wrapped.hasNext()
    }

    override fun peek(): Token {
        return wrapped.peek()
    }

    override fun nextName(): String {
        return wrapped.nextName()
    }

    override fun selectName(options: Options): Int {
        val index = wrapped.selectName(options)
        return if (index >= 0 && wrapped.peek() == Token.NULL) {
            wrapped.skipValue()
            ignoreSkipName.set(true)
            ignoreSkipValue.set(true)
            -1
        } else {
            index
        }
    }

    override fun skipName() {
        if (ignoreSkipName.compareAndSet(true, false)) {
            return
        }
        wrapped.skipName()
    }

    override fun nextString(): String {
        return wrapped.nextString()
    }

    override fun selectString(options: Options): Int {
        return wrapped.selectString(options)
    }

    override fun nextBoolean(): Boolean {
        return wrapped.nextBoolean()
    }

    override fun <T : Any?> nextNull(): T? {
        return wrapped.nextNull()
    }

    override fun nextDouble(): Double {
        return wrapped.nextDouble()
    }

    override fun nextLong(): Long {
        return wrapped.nextLong()
    }

    override fun nextInt(): Int {
        return wrapped.nextInt()
    }

    override fun nextSource(): BufferedSource {
        return wrapped.nextSource()
    }

    override fun skipValue() {
        if (ignoreSkipValue.compareAndSet(true, false)) {
            return
        }
        wrapped.skipValue()
    }

    override fun peekJson(): JsonReader {
        return wrapped.peekJson()
    }

    override fun promoteNameToValue() {
        wrapped.promoteNameToValue()
    }
}