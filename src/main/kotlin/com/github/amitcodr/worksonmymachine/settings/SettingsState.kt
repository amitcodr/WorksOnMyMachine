package com.github.amitcodr.worksonmymachine.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "WorksOnMyMachineSettings",
    storages = [Storage("WorksOnMyMachineSettings.xml")]
)
class SettingsState : PersistentStateComponent<SettingsState> {

    // Master toggle
    var enabled: Boolean = true

    // Sound file path (custom local sound)
    var soundPath: String = ""

    // Trigger options
    var playOnBuildSuccess: Boolean = true
    var playOnBuildFailure: Boolean = true
    var playOnRunSuccess: Boolean = false
    var playOnException: Boolean = true

    /**
     * Called by the IDE to save state
     */
    override fun getState(): SettingsState = this

    /**
     * Called by the IDE to restore saved state from disk
     */
    override fun loadState(state: SettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        /**
         * Global singleton access (Application-level service)
         */
        val instance: SettingsState
            get() = ApplicationManager.getApplication()
                .getService(SettingsState::class.java)
    }
}