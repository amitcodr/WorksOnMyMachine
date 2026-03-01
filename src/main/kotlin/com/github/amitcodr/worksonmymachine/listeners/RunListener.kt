package com.github.amitcodr.worksonmymachine.listeners

import com.github.amitcodr.worksonmymachine.sound.SoundManager
import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.util.Key

class RunListener : ExecutionListener {

    override fun processStarted(
        executorId: String,
        env: ExecutionEnvironment,
        handler: com.intellij.execution.process.ProcessHandler
    ) {
        handler.addProcessListener(object : ProcessAdapter() {

            override fun processTerminated(event: ProcessEvent) {
                // Exit code 0 = normal
                // Non-zero = crash / failure / force stop
                if (event.exitCode != 0) {
                    println("WorksOnMyMachine: Process crashed (exitCode=${event.exitCode})")
                    SoundManager.playException()
                } else {
                    SoundManager.playRunSuccess()
                }
            }

            override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                val text = event.text ?: return

                println("WorksOnMyMachine DEBUG: $text")

                // Extra fallback detection (for some devices/emulators)
                if (
                    text.contains("FATAL EXCEPTION") ||
                    text.contains("ANR in") ||
                    text.contains("has died")
                ) {
                    println("WorksOnMyMachine: Crash detected via logs")
                    SoundManager.playException()
                }
            }
        })
    }
}