package com.chestnut.aides.util

import com.chestnut.aides.conf.Env
import com.chestnut.aides.conf.SystemConf
import java.awt.Robot
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent

enum class TypingExpression {
    VERBATIM,
    ALL;
}

object AwtUtils {
    private var robot: Robot = Robot()

    fun writeToFocus(text: Array<String>, expression: TypingExpression = TypingExpression.VERBATIM) {
        when (expression) {
            TypingExpression.VERBATIM -> inputActForVerbatim(text)
            TypingExpression.ALL -> Array(1) { text }
        }
    }

    fun inputActForVerbatim(text: Array<String>){
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val originalContents = clipboard.getContents(null)
        for (item in text){
            if (item.startsWith("@keyEvent(")){
                val v = item.removeSurrounding("@keyEvent(",")").split("|").map {
                    val field = KeyEvent::class.java.getField(it)
                    field.getInt(null)
                }
                for (i in v) {
                    robot.keyPress(i)
                }
                for (i in v) {
                    robot.keyRelease(i)
                }
            }else{
                for (s in item.map { it.toString() }) {
                    clipboard.setContents(StringSelection(s), null)
                    pasteAction(robot)
                    clipboard.setContents(StringSelection(null), null)
                }
            }
        }
        clipboard.setContents(originalContents, null)
    }
}
private fun pasteAction(robot:Robot){
    when (SystemConf.env) {
        Env.WINDOWS,Env.LINUX -> {
            robot.keyPress(KeyEvent.VK_CONTROL)
            robot.keyPress(KeyEvent.VK_V)
            robot.keyRelease(KeyEvent.VK_V)
            robot.keyRelease(KeyEvent.VK_CONTROL)
        }
        Env.MAC -> {
            robot.keyPress(KeyEvent.VK_META)
            robot.keyPress(KeyEvent.VK_V)
            robot.keyRelease(KeyEvent.VK_V)
            robot.keyRelease(KeyEvent.VK_META)
        }
        else -> {
            throw Exception("unknow system")
        }
    }
}