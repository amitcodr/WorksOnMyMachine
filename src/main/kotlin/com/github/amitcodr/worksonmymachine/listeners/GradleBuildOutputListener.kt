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
        println("WorksOnMyMachine: Process started -> ${env.runProfile.name}")

        handler.addProcessListener(object : ProcessAdapter() {

            override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                val text = event.text ?: return

                // Debug log
                println("WorksOnMyMachine: Build output -> $text")

                // Detect Gradle build success
                if (text.contains("BUILD SUCCESSFUL")) {
                    println("WorksOnMyMachine: BUILD SUCCESS detected (Gradle output)")
                    SoundManager.playBuildSuccess()
                }

                // Detect Gradle build failure
                if (text.contains("BUILD FAILED")) {
                    println("WorksOnMyMachine: BUILD FAILURE detected (Gradle output)")
                    SoundManager.playBuildFailure()
                }
            }
        })
    }
}