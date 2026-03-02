package com.github.amitcodr.worksonmymachine.listeners

import com.github.amitcodr.worksonmymachine.sound.SoundManager
import com.intellij.build.BuildProgressListener
import com.intellij.build.events.BuildEvent
import com.intellij.build.events.FinishBuildEvent

class BuildListener : BuildProgressListener {

    override fun onEvent(buildId: Any, event: BuildEvent) {
        println("WorksOnMyMachine: Build event -> ${event.javaClass.simpleName}")

        if (event is FinishBuildEvent) {
            println("WorksOnMyMachine: Build FINISHED")

            val resultName = event.result.javaClass.simpleName
            println("WorksOnMyMachine: Result type -> $resultName")

            if (resultName.contains("Success", ignoreCase = true)) {
                println("WorksOnMyMachine: BUILD SUCCESS detected")
                SoundManager.playBuildSuccess()
            } else if (resultName.contains("Failure", ignoreCase = true)) {
                println("WorksOnMyMachine: BUILD FAILURE detected")
                SoundManager.playBuildFailure()
            }
        }
    }
}