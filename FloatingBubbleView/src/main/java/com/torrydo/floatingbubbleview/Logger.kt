package com.torrydo.floatingbubbleview

import android.util.Log

internal fun String?.toTag() = "<> $this"

internal interface Logger {

    @Deprecated("not implemented yet")
    fun setTagName(tagName: String)

    fun enableLogger(enabled: Boolean)

    fun d(message: String, tag: String? = javaClass.simpleName.toString())

    fun e(message: String, tag: String? = javaClass.simpleName.toString())
}

internal open class LoggerImpl : Logger {

    private var _tag: String? = null
    private var _isLoggerEnabled: Boolean = Const.IS_LOGGER_ENABLED


    override fun setTagName(tagName: String) {
        _tag = tagName.toTag()
    }

    override fun enableLogger(enabled: Boolean) {
        _isLoggerEnabled = enabled
    }

    override fun d(message: String, tag: String?) {
        if (_isLoggerEnabled) {
            Log.d(tag.toTag(), message)
        }
    }

    override fun e(message: String, tag: String?) {
        if (_isLoggerEnabled) {
            Log.e(tag.toTag(), message)
        }
    }


}