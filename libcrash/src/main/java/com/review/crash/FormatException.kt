package com.review.crash

import android.os.Build
import java.text.DateFormat
import java.util.*

class FormatException {

    val DEVICE = listOf("BRAND", "CPU_ABI", "DISPLAY", "SOC_MODEL", "TIME", "MODEL")
    val df1: DateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.CHINA)
    val df5 = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.CHINA)

    fun parseThreadInfo(thread: Thread): String {
        val index = 5
        val stackTrace = thread.stackTrace
        val fileName = stackTrace[index].fileName
        val className = stackTrace[index].className
        val methodName = stackTrace[index].methodName
        val lineNumber = stackTrace[index].lineNumber

        return String.format("文件:%s-%s-%s-第%s行", fileName, className, methodName, lineNumber)
    }

    fun deviceInfo(): String {
        val date = Date()
        val date1 = df1.format(date)
        val time1 = df5.format(date)
        val build = Build::class.java
        val instance = build.newInstance()
        return build.declaredFields.filter { DEVICE.contains(it.name) }
            .map {
                if (it.name == "TIME") {
                    "\n${it.name} :${date1} $time1"
                } else {
                    "\n${it.name} :${it.get(instance)}"
                }
            }
            .toString()
    }
}