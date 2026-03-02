package com.github.amitcodr.worksonmymachine.listeners

import com.github.amitcodr.worksonmymachine.sound.SoundManager
import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.util.Key

class GradleBuildOutputListener : ExecutionListener {

    override fun processStarted(
        executorId: String,
        env: ExecutionEnvironment,
        handler: com.intellij.execution.process.ProcessHandler
    ) {
        val profileName = env.runProfile.name
        println("WorksOnMyMachine: Process started -> $profileName")

        handler.addProcessListener(object : ProcessAdapter() {

            override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                val text = event.text ?: return

                // Debug all output
                println("WorksOnMyMachine: [$profileName] -> $text")

                // 🎯 Detect Gradle build success
                if (text.contains("BUILD SUCCESSFUL", ignoreCase = true)) {
                    println("WorksOnMyMachine: BUILD SUCCESS detected from Gradle")
                    SoundManager.playBuildSuccess()
                }

                // 🎯 Detect Gradle build failure
                if (text.contains("BUILD FAILED", ignoreCase = true)) {
                    println("WorksOnMyMachine: BUILD FAILURE detected from Gradle")
                    SoundManager.playBuildFailure()
                }

                // 🎯 Detect install success (Android specific strong signal)
                if (text.contains("Installing APK") ||
                    text.contains("Installed on") ||
                    text.contains("Connected to process")
                ) {
                    println("WorksOnMyMachine: APP RUN SUCCESS detected")
                    SoundManager.playRunSuccess()
                }
            }

            override fun processTerminated(event: ProcessEvent) {
                println("WorksOnMyMachine: Process terminated -> $profileName")
            }
        })
    }
}