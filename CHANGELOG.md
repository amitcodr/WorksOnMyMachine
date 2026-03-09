## 0.0.1

### Initial Release

WorksOnMyMachine adds sound effects to Android Studio builds and app runs.

### Features

- 🔊 Play sound when a build starts
- 🔊 Play sound when a build succeeds
- 🔊 Play sound when a build fails
- 🎧 Custom sound support for each build event
- 🎛 Simple settings page to configure sounds
- 🔁 Reset button to revert to default sounds
- 🧪 Test buttons to preview configured sounds
- 🛑 Automatically stops previous sound when a new one plays

### Default Sounds

If no custom sound is selected, the plugin will use built-in default sounds:

- `progress.wav` for build start
- `success.wav` for build success
- `failure.wav` for build failure

### System Requirements

| Requirement | Version |
|-------------|--------|
Android Studio | **2024.1 (Koala) or newer** |
Java Runtime | **JBR 17** (bundled with Android Studio) |
Operating System | Windows, macOS, Linux |

### Compatibility

Tested with:

- Android Studio **Koala (2024.1)**
- Android Studio **Koala Patch releases**

### Notes

- Works with **Gradle builds executed from Android Studio**
- Works when running apps via the **Run button**
- Supports **custom local WAV sound files**