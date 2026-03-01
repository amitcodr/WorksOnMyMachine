package com.github.amitcodr.worksonmymachine.listeners

import com.intellij.build.BuildProgressListener
import com.intellij.build.events.BuildEvent
import com.intellij.build.events.FinishBuildEvent
import com.intellij.build.events.impl.FailureResultImpl
import com.intellij.build.events.impl.SuccessResultImpl
import com.github.amitcodr.worksonmymachine.sound.SoundManager

class BuildListener : BuildProgressListener {

    override fun onEvent(buildId: Any, event: BuildEvent) {
        if (event is FinishBuildEvent) {
            when (event.result) {
                is SuccessResultImpl -> {
                    SoundManager.playBuildSuccess()
                }
                is FailureResultImpl -> {
                    SoundManager.playBuildFailure()
                }
            }
        }
    }
}