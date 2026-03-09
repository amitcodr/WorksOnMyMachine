package com.github.amitcodr.worksonmymachine.sound

import com.github.amitcodr.worksonmymachine.settings.SettingsState
import java.io.File
import javax.sound.sampled.*
import kotlin.concurrent.thread

object SoundManager {

    private const val TAG = "WorksOnMyMachine"
    private var currentClip: Clip? = null

    /*
     * PUBLIC EVENTS
     */

    fun playBuildStart() {
        val state = SettingsState.instance
        if (!state.enabled) return

        play(
            customPath = state.buildStartSound,
            defaultResource = "/sounds/progress.wav",
            source = "BUILD_START"
        )
    }

    fun playBuildSuccess() {
        val state = SettingsState.instance
        if (!state.enabled) return

        play(
            customPath = state.buildSuccessSound,
            defaultResource = "/sounds/success.wav",
            source = "BUILD_SUCCESS"
        )
    }

    fun playBuildFailure() {
        val state = SettingsState.instance
        if (!state.enabled) return

        play(
            customPath = state.buildFailureSound,
            defaultResource = "/sounds/failure.wav",
            source = "BUILD_FAILURE"
        )
    }

    /*
     * SETTINGS TEST BUTTON SUPPORT
     */

    fun testFile(path: String) {
        play(
            customPath = path,
            defaultResource = null,
            source = "TEST_CUSTOM_FILE"
        )
    }

    fun playDefaultStart() {
        play(null, "/sounds/progress.wav", "TEST_DEFAULT_START")
    }

    fun playDefaultSuccess() {
        play(null, "/sounds/success.wav", "TEST_DEFAULT_SUCCESS")
    }

    fun playDefaultFailure() {
        play(null, "/sounds/failure.wav", "TEST_DEFAULT_FAILURE")
    }

    /*
     * CORE PLAYBACK ENGINE
     */

    private fun play(
        customPath: String?,
        defaultResource: String?,
        source: String
    ) {

        log("play() triggered from $source")

        thread(start = true, isDaemon = true, name = "WorksOnMyMachine-Audio") {

            try {

                val audioInputStream = when {
                    !customPath.isNullOrBlank() && File(customPath).exists() -> {
                        log("Using custom sound: $customPath")
                        AudioSystem.getAudioInputStream(File(customPath))
                    }

                    defaultResource != null -> {
                        log("Using default sound: $defaultResource")
                        val url = javaClass.getResource(defaultResource)
                            ?: throw Exception("Default sound resource not found: $defaultResource")

                        AudioSystem.getAudioInputStream(url)
                    }

                    else -> {
                        logError("No sound source available")
                        return@thread
                    }
                }

                val newClip = AudioSystem.getClip()

                newClip.open(audioInputStream)

                synchronized(this) {

                    // 🔴 STOP PREVIOUS SOUND
                    currentClip?.let {
                        if (it.isRunning) {
                            log("Stopping previous sound")
                            it.stop()
                        }
                        it.close()
                    }

                    currentClip = newClip
                }

                newClip.addLineListener { event ->
                    if (event.type == LineEvent.Type.STOP) {
                        newClip.close()
                    }
                }

                newClip.start()

                log("Playback started")

            } catch (e: Exception) {
                logError("Playback failure", e)
            }
        }
    }

    /*
     * LOGGING
     */

    private fun log(message: String) {
        println("[$TAG] $message")
    }

    private fun logError(message: String, throwable: Throwable? = null) {
        println("[$TAG][ERROR] $message")
        throwable?.printStackTrace()
    }
}