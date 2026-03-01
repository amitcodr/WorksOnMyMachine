package com.github.amitcodr.worksonmymachine.listeners

import com.github.amitcodr.worksonmymachine.sound.SoundManager
import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.util.Key

class ExecutionBusListener : ExecutionListener {

    override fun processStarted(
        executorId: String,
        env: ExecutionEnvironment,
        handler: com.intellij.execution.process.ProcessHandler
    ) {
        println("WorksOnMyMachine: Process STARTED -> ${env.runProfile.name}")

        handler.addProcessListener(object : ProcessAdapter() {

            override fun startNotified(event: ProcessEvent) {
                println("WorksOnMyMachine: Process startNotified")
            }

            override fun processTerminated(event: ProcessEvent) {
                println("WorksOnMyMachine: Process TERMINATED exitCode=${event.exitCode}")

                // Android apps often return 0 even on crash
                SoundManager.playRunSuccess()
            }

            override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                val text = event.text ?: return

                // Critical Android crash detection
                if (
                    text.contains("FATAL EXCEPTION") ||
                    text.contains("ANR in") ||
                    text.contains("has died")
                ) {
                    println("WorksOnMyMachine: ANDROID CRASH DETECTED")
                    SoundManager.playException()
                }
            }
        })
    }
}