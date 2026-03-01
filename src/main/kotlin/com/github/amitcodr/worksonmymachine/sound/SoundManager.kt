package com.github.amitcodr.worksonmymachine.sound

import com.github.amitcodr.worksonmymachine.settings.SettingsState
import java.io.File
import javax.sound.sampled.*
import kotlin.concurrent.thread

object SoundManager {

    private const val TAG = "WorksOnMyMachine"

    // Public entry points (called by listeners)
    fun playBuildSuccess() {
        val state = SettingsState.instance
        log("playBuildSuccess() called")

        if (!state.enabled) {
            log("Sound is globally disabled")
            return
        }

        if (!state.playOnBuildSuccess) {
            log("Build success sound disabled in settings")
            return
        }

        play(state.soundPath, "BUILD_SUCCESS")
    }

    fun playBuildFailure() {
        val state = SettingsState.instance
        log("playBuildFailure() called")

        if (!state.enabled) {
            log("Sound is globally disabled")
            return
        }

        if (!state.playOnBuildFailure) {
            log("Build failure sound disabled in settings")
            return
        }

        play(state.soundPath, "BUILD_FAILURE")
    }

    fun playRunSuccess() {
        val state = SettingsState.instance
        log("playRunSuccess() called")

        if (!state.enabled) {
            log("Sound is globally disabled")
            return
        }

        if (!state.playOnRunSuccess) {
            log("Run success sound disabled in settings")
            return
        }

        play(state.soundPath, "RUN_SUCCESS")
    }

    fun playException() {
        val state = SettingsState.instance
        log("playException() called")

        if (!state.enabled) {
            log("Sound is globally disabled")
            return
        }

        if (!state.playOnException) {
            log("Exception sound disabled in settings")
            return
        }

        play(state.soundPath, "EXCEPTION")
    }

    /**
     * Used by Test Audio button
     */
    fun testSound() {
        val state = SettingsState.instance
        log("testSound() button pressed")

        if (!state.enabled) {
            log("Sound disabled in settings - cannot test")
            return
        }

        play(state.soundPath, "MANUAL_TEST")
    }

    /**
     * Core sound playback (non-blocking + heavily logged)
     */
    private fun play(filePath: String?, source: String) {
        log("play() invoked from: $source")
        log("Received path: $filePath")

        if (filePath.isNullOrBlank()) {
            logError("Sound path is NULL or blank")
            return
        }

        val file = File(filePath)

        if (!file.exists()) {
            logError("Sound file DOES NOT exist at path: ${file.absolutePath}")
            return
        }

        if (!file.canRead()) {
            logError("Sound file exists but is NOT readable")
            return
        }

        log("Sound file validated. Size=${file.length()} bytes")
        log("Starting background playback thread...")

        // Play on background thread to avoid freezing IDE UI
        thread(start = true, isDaemon = true, name = "WorksOnMyMachine-Audio") {
            try {
                log("Opening audio input stream...")

                val audioInputStream = AudioSystem.getAudioInputStream(file)

                log("Audio format: ${audioInputStream.format}")

                val clip: Clip = AudioSystem.getClip()
                log("Clip acquired successfully")

                clip.open(audioInputStream)
                log("Clip opened successfully")

                // Auto release resources after playback (VERY important for IDE plugins)
                clip.addLineListener(object : LineListener {
                    override fun update(event: LineEvent) {
                        log("LineEvent received: ${event.type}")

                        if (event.type == LineEvent.Type.START) {
                            log("Playback STARTED")
                        }

                        if (event.type == LineEvent.Type.STOP) {
                            log("Playback STOPPED, closing clip")
                            clip.close()
                        }
                    }
                })

                log("Starting clip playback NOW")
                clip.start()

            } catch (e: UnsupportedAudioFileException) {
                logError("Unsupported audio format. Use WAV (PCM 16-bit recommended).", e)
            } catch (e: LineUnavailableException) {
                logError("Audio line unavailable (IDE audio system issue).", e)
            } catch (e: Exception) {
                logError("Unexpected error during sound playback.", e)
            }
        }
    }

    private fun log(message: String) {
        println("[$TAG] $message")
    }

    private fun logError(message: String, throwable: Throwable? = null) {
        println("[$TAG][ERROR] $message")
        throwable?.printStackTrace()
    }
}