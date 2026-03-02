package com.github.amitcodr.worksonmymachine.settings

import com.github.amitcodr.worksonmymachine.sound.SoundManager
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import java.io.File
import javax.swing.*

class SettingsConfigurable : Configurable {

    private var rootPanel: JPanel? = null
    private var enableCheckBox: JCheckBox? = null
    private var soundField: TextFieldWithBrowseButton? = null
    private var statusLabel: JLabel? = null

    private var buildSuccess: JCheckBox? = null
    private var buildFailure: JCheckBox? = null
    private var runSuccess: JCheckBox? = null
    private var exception: JCheckBox? = null

    override fun getDisplayName(): String = "WorksOnMyMachine"

    override fun createComponent(): JComponent {
        rootPanel = JPanel()
        rootPanel!!.layout = BoxLayout(rootPanel, BoxLayout.Y_AXIS)
        rootPanel!!.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        // 🔘 Master Toggle
        enableCheckBox = JCheckBox("Enable Sound Effects")

        // 🎵 Sound Section Title
        val soundTitle = JLabel("Sound Configuration")
        soundTitle.font = soundTitle.font.deriveFont(16f)

        soundField = TextFieldWithBrowseButton()
        soundField!!.addBrowseFolderListener(
            "Select Sound File",
            "Choose a WAV sound file (recommended for best compatibility)",
            null,
            FileChooserDescriptor(true, false, false, false, false, false)
        )

        val testButton = JButton("🔊 Test Audio")
        testButton.toolTipText = "Play the selected sound file"

        statusLabel = JLabel(" ")
        statusLabel!!.font = statusLabel!!.font.deriveFont(12f)

        testButton.addActionListener {
            val path = soundField!!.text
            if (path.isBlank() || !File(path).exists()) {
                statusLabel!!.text = "❌ Invalid sound file path"
                return@addActionListener
            }
            statusLabel!!.text = "🔊 Playing sound..."
            SoundManager.testSound()
        }

        // 🎛 Trigger Section
        val triggerTitle = JLabel("Sound Triggers")
        triggerTitle.font = triggerTitle.font.deriveFont(16f)

        val reliabilityNote = JLabel(
            "<html><i>Note: Build events are 100% reliable. " +
                    "Run/Crash detection depends on Android Studio runtime behavior.</i></html>"
        )

        buildSuccess = JCheckBox("Play on Build Success (Recommended)")
        buildFailure = JCheckBox("Play on Build Failure (Recommended)")
        runSuccess = JCheckBox("Play on App Run (Best effort)")
        exception = JCheckBox("Play on Runtime Crash (Best effort)")

        // Layout
        rootPanel!!.add(enableCheckBox)
        rootPanel!!.add(Box.createVerticalStrut(15))

        rootPanel!!.add(soundTitle)
        rootPanel!!.add(Box.createVerticalStrut(5))
        rootPanel!!.add(JLabel("Custom Sound File (.wav recommended):"))
        rootPanel!!.add(soundField)
        rootPanel!!.add(Box.createVerticalStrut(8))
        rootPanel!!.add(testButton)
        rootPanel!!.add(Box.createVerticalStrut(5))
        rootPanel!!.add(statusLabel)

        rootPanel!!.add(Box.createVerticalStrut(20))
        rootPanel!!.add(triggerTitle)
        rootPanel!!.add(Box.createVerticalStrut(5))
        rootPanel!!.add(reliabilityNote)
        rootPanel!!.add(Box.createVerticalStrut(10))
        rootPanel!!.add(buildSuccess)
        rootPanel!!.add(buildFailure)
        rootPanel!!.add(runSuccess)
        rootPanel!!.add(exception)

        return rootPanel!!
    }

    override fun isModified(): Boolean {
        val state = SettingsState.instance
        return enableCheckBox!!.isSelected != state.enabled ||
                soundField!!.text != state.soundPath ||
                buildSuccess!!.isSelected != state.playOnBuildSuccess ||
                buildFailure!!.isSelected != state.playOnBuildFailure ||
                runSuccess!!.isSelected != state.playOnRunSuccess ||
                exception!!.isSelected != state.playOnException
    }

    override fun apply() {
        val state = SettingsState.instance
        state.enabled = enableCheckBox!!.isSelected
        state.soundPath = soundField!!.text
        state.playOnBuildSuccess = buildSuccess!!.isSelected
        state.playOnBuildFailure = buildFailure!!.isSelected
        state.playOnRunSuccess = runSuccess!!.isSelected
        state.playOnException = exception!!.isSelected
    }

    override fun reset() {
        val state = SettingsState.instance
        enableCheckBox!!.isSelected = state.enabled
        soundField!!.text = state.soundPath
        buildSuccess!!.isSelected = state.playOnBuildSuccess
        buildFailure!!.isSelected = state.playOnBuildFailure
        runSuccess!!.isSelected = state.playOnRunSuccess
        exception!!.isSelected = state.playOnException
        statusLabel!!.text = " "
    }

    override fun disposeUIResources() {
        rootPanel = null
        enableCheckBox = null
        soundField = null
        statusLabel = null
        buildSuccess = null
        buildFailure = null
        runSuccess = null
        exception = null
    }
}