package com.github.amitcodr.worksonmymachine.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import javax.swing.*

class SettingsConfigurable : Configurable {

    private var panel: JPanel? = null
    private var enableCheckBox: JCheckBox? = null
    private var soundField: TextFieldWithBrowseButton? = null
    private var buildSuccess: JCheckBox? = null
    private var buildFailure: JCheckBox? = null
    private var runSuccess: JCheckBox? = null
    private var exception: JCheckBox? = null

    override fun getDisplayName(): String = "WorksOnMyMachine"

    override fun createComponent(): JComponent {
        panel = JPanel()
        panel!!.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        enableCheckBox = JCheckBox("Enable Sound Effects")

        soundField = TextFieldWithBrowseButton()
        soundField!!.addBrowseFolderListener(
            "Select Sound File",
            "Choose a WAV sound file (recommended)",
            null,
            FileChooserDescriptor(true, false, false, false, false, false)
        )

        val testButton = JButton("🔊 Test Audio")

        testButton.addActionListener {
            println("WorksOnMyMachine: Test button pressed")
            com.github.amitcodr.worksonmymachine.sound.SoundManager.testSound()
        }

        buildSuccess = JCheckBox("Play on Build Success")
        buildFailure = JCheckBox("Play on Build Failure")
        runSuccess = JCheckBox("Play on Run Success")
        exception = JCheckBox("Play on Runtime Exception (Crash)")

        panel!!.add(enableCheckBox)
        panel!!.add(Box.createVerticalStrut(10))
        panel!!.add(JLabel("Custom Sound File:"))
        panel!!.add(soundField)
        panel!!.add(Box.createVerticalStrut(8))
        panel!!.add(testButton) // ⭐ NEW BUTTON
        panel!!.add(Box.createVerticalStrut(15))
        panel!!.add(JLabel("Play sound when:"))
        panel!!.add(buildSuccess)
        panel!!.add(buildFailure)
        panel!!.add(runSuccess)
        panel!!.add(exception)

        return panel!!
    }

    // 🔴 CRITICAL FIX: Proper modification detection
    override fun isModified(): Boolean {
        val state = SettingsState.instance
        return enableCheckBox!!.isSelected != state.enabled ||
                soundField!!.text != state.soundPath ||
                buildSuccess!!.isSelected != state.playOnBuildSuccess ||
                buildFailure!!.isSelected != state.playOnBuildFailure ||
                runSuccess!!.isSelected != state.playOnRunSuccess ||
                exception!!.isSelected != state.playOnException
    }

    // Saves settings when user clicks OK / Apply
    override fun apply() {
        val state = SettingsState.instance
        state.enabled = enableCheckBox!!.isSelected
        state.soundPath = soundField!!.text
        state.playOnBuildSuccess = buildSuccess!!.isSelected
        state.playOnBuildFailure = buildFailure!!.isSelected
        state.playOnRunSuccess = runSuccess!!.isSelected
        state.playOnException = exception!!.isSelected
    }

    // Loads saved settings when page opens
    override fun reset() {
        val state = SettingsState.instance
        enableCheckBox!!.isSelected = state.enabled
        soundField!!.text = state.soundPath
        buildSuccess!!.isSelected = state.playOnBuildSuccess
        buildFailure!!.isSelected = state.playOnBuildFailure
        runSuccess!!.isSelected = state.playOnRunSuccess
        exception!!.isSelected = state.playOnException
    }

    override fun disposeUIResources() {
        panel = null
        enableCheckBox = null
        soundField = null
        buildSuccess = null
        buildFailure = null
        runSuccess = null
        exception = null
    }
}