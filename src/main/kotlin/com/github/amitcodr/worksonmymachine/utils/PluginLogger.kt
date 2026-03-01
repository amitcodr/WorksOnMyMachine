package com.github.amitcodr.worksonmymachine.utils

import com.intellij.openapi.diagnostic.Logger

object PluginLogger {
    private val logger = Logger.getInstance("WorksOnMyMachine")

    fun info(message: String) {
        logger.info("[WorksOnMyMachine] $message")
        println("[WorksOnMyMachine] $message") // visible in sandbox console too
    }

    fun error(message: String, throwable: Throwable? = null) {
        logger.error("[WorksOnMyMachine] $message", throwable)
        println("[WorksOnMyMachine][ERROR] $message")
        throwable?.printStackTrace()
    }
}