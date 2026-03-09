package com.github.amitcodr.worksonmymachine.listeners

import com.github.amitcodr.worksonmymachine.sound.SoundManager
import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.util.Key

class GradleBuildOutputListener : ExecutionListener {

    private var buildRunning = false

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

                // Debug output
                println("WorksOnMyMachine: [$profileName] -> $text")

                /*
                 * Detect build start
                 * Gradle prints task lines like:
                 * > Task :app:compileDebugKotlin
                 */
                if (!buildRunning && text.contains("> Task")) {
                    buildRunning = true
                    println("WorksOnMyMachine: BUILD START detected")
                    SoundManager.playBuildStart()
                }

                /*
                 * Detect Gradle success
                 */
                if (text.contains("BUILD SUCCESSFUL", ignoreCase = true)) {
                    println("WorksOnMyMachine: BUILD SUCCESS detected")
                    SoundManager.playBuildSuccess()
                    buildRunning = false
                }

                /*
                 * Detect Gradle failure
                 */
                if (text.contains("BUILD FAILED", ignoreCase = true)) {
                    println("WorksOnMyMachine: BUILD FAILURE detected")
                    SoundManager.playBuildFailure()
                    buildRunning = false
                }

                /*
                 * Detect Android run success
                 * This happens after successful build
                 */
                if (text.contains("Connected to process")) {
                    println("WorksOnMyMachine: APP RUN SUCCESS detected")
                    SoundManager.playBuildSuccess()
                }
            }

            override fun processTerminated(event: ProcessEvent) {
                println("WorksOnMyMachine: Process terminated -> $profileName")

                // Safety reset
                buildRunning = false
            }
        })
    }
}