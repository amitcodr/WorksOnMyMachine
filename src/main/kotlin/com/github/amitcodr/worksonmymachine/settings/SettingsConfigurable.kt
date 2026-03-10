package com.github.amitcodr.worksonmymachine.settings

import com.github.amitcodr.worksonmymachine.sound.SoundManager
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import java.io.File
import javax.swing.*

class SettingsConfigurable : Configurable {

    private var panel: JPanel? = null

    private var enablePlugin: JCheckBox? = null

    private var enableStart: JCheckBox? = null
    private var enableSuccess: JCheckBox? = null
    private var enableFailure: JCheckBox? = null

    private var startField: TextFieldWithBrowseButton? = null
    private var successField: TextFieldWithBrowseButton? = null
    private var failureField: TextFieldWithBrowseButton? = null

    private var volumeSlider: JSlider? = null
    private var volumeLabel: JBLabel? = null

    override fun getDisplayName(): String = "WorksOnMyMachine"

    override fun createComponent(): JComponent {

        val descriptor = FileChooserDescriptor(true, false, false, false, false, false)

        /* MASTER ENABLE */

        val enablePluginLocal = JCheckBox("Enable Sound Effects")
        enablePlugin = enablePluginLocal

        /* ENABLE CHECKBOXES */

        val enableStartLocal = JCheckBox("Enable Build Start Sound")
        val enableSuccessLocal = JCheckBox("Enable Build Success Sound")
        val enableFailureLocal = JCheckBox("Enable Build Failure Sound")

        enableStart = enableStartLocal
        enableSuccess = enableSuccessLocal
        enableFailure = enableFailureLocal

        /* FILE SELECTORS */

        val startFieldLocal = TextFieldWithBrowseButton()
        val successFieldLocal = TextFieldWithBrowseButton()
        val failureFieldLocal = TextFieldWithBrowseButton()

        startField = startFieldLocal
        successField = successFieldLocal
        failureField = failureFieldLocal

        startFieldLocal.addBrowseFolderListener(
            "Select Build Start Sound",
            "Choose a sound file",
            null,
            descriptor
        )

        successFieldLocal.addBrowseFolderListener(
            "Select Build Success Sound",
            "Choose a sound file",
            null,
            descriptor
        )

        failureFieldLocal.addBrowseFolderListener(
            "Select Build Failure Sound",
            "Choose a sound file",
            null,
            descriptor
        )

        /* TEST BUTTONS */

        val startTest = JButton("Test")
        val successTest = JButton("Test")
        val failureTest = JButton("Test")

        startTest.addActionListener {
            val path = startFieldLocal.text
            if (File(path).exists()) SoundManager.testFile(path)
            else SoundManager.playDefaultStart()
        }

        successTest.addActionListener {
            val path = successFieldLocal.text
            if (File(path).exists()) SoundManager.testFile(path)
            else SoundManager.playDefaultSuccess()
        }

        failureTest.addActionListener {
            val path = failureFieldLocal.text
            if (File(path).exists()) SoundManager.testFile(path)
            else SoundManager.playDefaultFailure()
        }

        /* VOLUME */

        val volumeSliderLocal = JSlider(0, 100, 80)
        val volumeLabelLocal = JBLabel("Volume: 80%")

        volumeSliderLocal.majorTickSpacing = 20
        volumeSliderLocal.paintTicks = true
        volumeSliderLocal.paintLabels = true

        volumeSliderLocal.addChangeListener {
            volumeLabelLocal.text = "Volume: ${volumeSliderLocal.value}%"
        }

        volumeSlider = volumeSliderLocal
        volumeLabel = volumeLabelLocal

        /* RESET BUTTON */

        val resetButton = JButton("Reset to Default Sounds")

        resetButton.addActionListener {

            val result = JOptionPane.showConfirmDialog(
                panel,
                "Reset all custom sound effects?",
                "Reset Sounds",
                JOptionPane.YES_NO_OPTION
            )

            if (result == JOptionPane.YES_OPTION) {
                startFieldLocal.text = ""
                successFieldLocal.text = ""
                failureFieldLocal.text = ""
            }
        }

        val hint = JBLabel(
            "<html><i>If no custom sound is selected, default plugin sounds will be used.</i></html>"
        )

        /* BUILD UI */

        panel = FormBuilder.createFormBuilder()

            .addComponent(enablePluginLocal)

            .addSeparator()

            .addComponent(enableStartLocal)
            .addLabeledComponent("Sound File:", startFieldLocal)
            .addComponent(startTest)

            .addComponent(enableSuccessLocal)
            .addLabeledComponent("Sound File:", successFieldLocal)
            .addComponent(successTest)

            .addComponent(enableFailureLocal)
            .addLabeledComponent("Sound File:", failureFieldLocal)
            .addComponent(failureTest)

            .addSeparator()

            .addComponent(volumeLabelLocal)
            .addComponent(volumeSliderLocal)

            .addSeparator()

            .addComponent(resetButton)
            .addComponent(hint)

            .panel

        return panel!!
    }

    override fun isModified(): Boolean {

        val state = SettingsState.instance

        return enablePlugin!!.isSelected != state.enabled ||
                enableStart!!.isSelected != state.enableStart ||
                enableSuccess!!.isSelected != state.enableSuccess ||
                enableFailure!!.isSelected != state.enableFailure ||
                startField!!.text != state.buildStartSound ||
                successField!!.text != state.buildSuccessSound ||
                failureField!!.text != state.buildFailureSound ||
                volumeSlider!!.value != state.volume
    }

    override fun apply() {

        val state = SettingsState.instance

        state.enabled = enablePlugin!!.isSelected

        state.enableStart = enableStart!!.isSelected
        state.enableSuccess = enableSuccess!!.isSelected
        state.enableFailure = enableFailure!!.isSelected

        state.buildStartSound = startField!!.text
        state.buildSuccessSound = successField!!.text
        state.buildFailureSound = failureField!!.text

        state.volume = volumeSlider!!.value
    }

    override fun reset() {

        val state = SettingsState.instance

        enablePlugin!!.isSelected = state.enabled

        enableStart!!.isSelected = state.enableStart
        enableSuccess!!.isSelected = state.enableSuccess
        enableFailure!!.isSelected = state.enableFailure

        startField!!.text = state.buildStartSound
        successField!!.text = state.buildSuccessSound
        failureField!!.text = state.buildFailureSound

        volumeSlider!!.value = state.volume
        volumeLabel!!.text = "Volume: ${state.volume}%"
    }

    override fun disposeUIResources() {
        panel = null
    }
}