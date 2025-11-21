# Kilma Android App

Android application to control Raspberry Pi running Proxmox hypervisor and various services.

> ⚠️ **Work in Progress**: This is a template application that cannot be fully built or used until the Raspberry Pi 5 server is installed and configured (planned by end of 2025). The UI is complete, but backend API integration is pending server deployment.

## Features

### 🖥️ Hypervisor Control
- Performance dashboard showing CPU, memory, and disk usage
- VM control buttons (Start, Shutdown, Force Stop)
- Real-time monitoring of Proxmox hypervisor

### 🐳 Docker Management
- Docker container controls
- Container status monitoring
- Quick container actions

### ⛏️ Minecraft Server Control
- Pre-programmed commands (Creative/Survival mode)
- Server restart functionality
- Quick access to common server commands

### 🏠 Home Automation
- Blank template ready for integration
- Customizable for future home automation features

## Tech Stack

- **Language**: Kotlin
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Architecture**: MVVM with Android Architecture Components
- **UI**: Material Design 3 with Bottom Navigation
- **Networking**: Retrofit + OkHttp (prepared for API integration)

## Project Structure

```
app/
├── src/main/
│   ├── java/com/kilma/raspberrypi/
│   │   ├── MainActivity.kt
│   │   └── ui/
│   │       ├── hypervisor/
│   │       ├── docker/
│   │       ├── minecraft/
│   │       └── homeautomation/
│   └── res/
│       ├── layout/
│       ├── navigation/
│       └── menu/
```

## Setup

1. Ensure you have Android Studio installed
2. Open the project in Android Studio
3. Sync Gradle dependencies
4. Run on emulator or physical device

## Building

```bash
./gradlew assembleDebug
```

## Future Enhancements

- API integration with Raspberry Pi backend
- Real-time performance graphs
- Push notifications for system alerts
- SSH terminal integration
- Custom Minecraft command builder
