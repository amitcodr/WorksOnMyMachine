package com.github.amitcodr.worksonmymachine.startup

import com.github.amitcodr.worksonmymachine.listeners.GradleBuildOutputListener
import com.intellij.execution.ExecutionManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class PluginStartup : StartupActivity {

    override fun runActivity(project: Project) {
        println("WorksOnMyMachine: Plugin STARTED for project -> ${project.name}")

        val connection = project.messageBus.connect(project)

        connection.subscribe(
            ExecutionManager.EXECUTION_TOPIC,
            GradleBuildOutputListener()
        )

        println("WorksOnMyMachine: GradleBuildOutputListener registered")
    }
}