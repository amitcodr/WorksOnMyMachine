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

    private var startSoundField: TextFieldWithBrowseButton? = null
    private var successSoundField: TextFieldWithBrowseButton? = null
    private var failureSoundField: TextFieldWithBrowseButton? = null

    override fun getDisplayName(): String = "WorksOnMyMachine"

    override fun createComponent(): JComponent {

        rootPanel = JPanel()
        rootPanel!!.layout = BoxLayout(rootPanel, BoxLayout.Y_AXIS)
        rootPanel!!.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        enableCheckBox = JCheckBox("Enable Sound Effects")

        val descriptor = FileChooserDescriptor(true, false, false, false, false, false)

        val title = JLabel("Build Sounds")
        title.font = title.font.deriveFont(16f)

        startSoundField = TextFieldWithBrowseButton()
        successSoundField = TextFieldWithBrowseButton()
        failureSoundField = TextFieldWithBrowseButton()

        startSoundField!!.addBrowseFolderListener(
            "Select Build Start Sound",
            "Choose a sound file",
            null,
            descriptor
        )

        successSoundField!!.addBrowseFolderListener(
            "Select Build Success Sound",
            "Choose a sound file",
            null,
            descriptor
        )

        failureSoundField!!.addBrowseFolderListener(
            "Select Build Failure Sound",
            "Choose a sound file",
            null,
            descriptor
        )

        val startTest = JButton("Test")
        val successTest = JButton("Test")
        val failureTest = JButton("Test")

        startTest.addActionListener {
            val path = startSoundField!!.text
            if (File(path).exists()) {
                SoundManager.testFile(path)
            } else {
                SoundManager.playDefaultStart()
            }
        }

        successTest.addActionListener {
            val path = successSoundField!!.text
            if (File(path).exists()) {
                SoundManager.testFile(path)
            } else {
                SoundManager.playDefaultSuccess()
            }
        }

        failureTest.addActionListener {
            val path = failureSoundField!!.text
            if (File(path).exists()) {
                SoundManager.testFile(path)
            } else {
                SoundManager.playDefaultFailure()
            }
        }

        val resetButton = JButton("Reset to Default Sounds")
        resetButton.toolTipText = "Remove all custom sound effects"

        resetButton.addActionListener {

            val result = JOptionPane.showConfirmDialog(
                rootPanel,
                "Reset all custom sound effects to plugin defaults?",
                "Reset Sounds",
                JOptionPane.YES_NO_OPTION
            )

            if (result == JOptionPane.YES_OPTION) {
                startSoundField!!.text = ""
                successSoundField!!.text = ""
                failureSoundField!!.text = ""
            }
        }

        rootPanel!!.add(enableCheckBox)
        rootPanel!!.add(Box.createVerticalStrut(15))

        rootPanel!!.add(title)
        rootPanel!!.add(Box.createVerticalStrut(10))

        rootPanel!!.add(createSoundRow("Build Start Sound:", startSoundField!!, startTest))
        rootPanel!!.add(createSoundRow("Build Success Sound:", successSoundField!!, successTest))
        rootPanel!!.add(createSoundRow("Build Failure Sound:", failureSoundField!!, failureTest))

        rootPanel!!.add(Box.createVerticalStrut(10))
        rootPanel!!.add(resetButton)

        rootPanel!!.add(Box.createVerticalStrut(15))

        val hint = JLabel(
            "<html><i>If no custom sound is selected, the plugin will use default sounds.</i></html>"
        )

        rootPanel!!.add(hint)

        return rootPanel!!
    }

    private fun createSoundRow(
        labelText: String,
        field: TextFieldWithBrowseButton,
        button: JButton
    ): JPanel {

        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)

        val label = JLabel(labelText)
        label.preferredSize = java.awt.Dimension(150, 25)

        panel.add(label)
        panel.add(field)
        panel.add(Box.createHorizontalStrut(5))
        panel.add(button)

        return panel
    }

    override fun isModified(): Boolean {

        val state = SettingsState.instance

        return enableCheckBox!!.isSelected != state.enabled ||
                startSoundField!!.text != state.buildStartSound ||
                successSoundField!!.text != state.buildSuccessSound ||
                failureSoundField!!.text != state.buildFailureSound
    }

    override fun apply() {

        val state = SettingsState.instance

        state.enabled = enableCheckBox!!.isSelected
        state.buildStartSound = startSoundField!!.text
        state.buildSuccessSound = successSoundField!!.text
        state.buildFailureSound = failureSoundField!!.text
    }

    override fun reset() {

        val state = SettingsState.instance

        enableCheckBox!!.isSelected = state.enabled

        startSoundField!!.text = state.buildStartSound
        successSoundField!!.text = state.buildSuccessSound
        failureSoundField!!.text = state.buildFailureSound
    }

    override fun disposeUIResources() {
        rootPanel = null
    }
}