package com.review.crash

import android.content.Context
import android.os.Build
import android.util.Log
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toOkioPath
import okio.buffer
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

/**
 * author：yangweichao@reworldgame.com
 * data: 2022/11/16 17:02
 * 本地崩溃记录
 */
object Crashlytics {

    /**
     * debug开关
     */
    private var debugSwitch = false

    fun openDebug() {
        debugSwitch = true
    }

    @JvmOverloads
    fun init(context: Context, parentPath: String? = null) {
        if (debugSwitch) {
            LocalExceptionHandler().init(context, parentPath)
        } else {
            Log.i("Crashlytics", "debugSwitch is $debugSwitch")
        }
    }


    internal class LocalExceptionHandler : Thread.UncaughtExceptionHandler {
        private lateinit var context: Context

        //默认路径/data/packagename/cache
        private var mParentPath = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.toString()

        // 系统默认的UncaughtException处理类
        private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

        fun init(context: Context, parentPath: String? = null) {
            this.context = context
            this.mParentPath = parentPath ?: mParentPath
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler(this)
        }


        override fun uncaughtException(t: Thread, e: Throwable) {
            try {
                saveToInnerMemory(t, e)
            } finally {
                mDefaultHandler?.uncaughtException(t, e)
            }
        }

        private fun saveToInnerMemory(errorThread: Thread, e: Throwable) {
            val fileName = "${
                Build.MODEL.replace(
                    " ",
                    ""
                )
            }-${SimpleDateFormat("yy-MM-dd HH:mm:ss").format(Date())}.txt"
            val path = File(mParentPath, fileName).let { file ->
                if (!file.exists()) {
                    file.createNewFile()
                }
                Path.Companion.let {
                    file.toOkioPath()
                }
            }
            FileSystem.SYSTEM.sink(path).buffer().use { sink ->
                sink.writeUtf8(FormatException().deviceInfo())
                sink.writeUtf8(e.stackTraceToString())
            }
        }
    }
}





