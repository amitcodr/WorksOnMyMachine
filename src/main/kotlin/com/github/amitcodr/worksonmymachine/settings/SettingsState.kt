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

    /**
     * Master enable/disable switch
     */
    var enabled: Boolean = true

    /**
     * Custom sound overrides
     * If empty → plugin uses default sounds from resources
     */
    var buildStartSound: String = ""
    var buildSuccessSound: String = ""
    var buildFailureSound: String = ""

    /**
     * Called by IDE when saving state
     */
    override fun getState(): SettingsState = this

    /**
     * Called by IDE when restoring state
     */
    override fun loadState(state: SettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        /**
         * Global singleton access
         */
        val instance: SettingsState
            get() = ApplicationManager.getApplication()
                .getService(SettingsState::class.java)
    }
}