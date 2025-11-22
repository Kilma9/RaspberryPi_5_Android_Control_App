# Kilma Android App

Android application to control Raspberry Pi running Proxmox hypervisor and MikroTik router.

> ⚠️ **Work in Progress**: This is a template application. Raspberry Pi integration is pending server deployment (planned by end of 2025). MikroTik control is functional via HTTP REST API.

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

### 🌐 MikroTik Router Control
- **Status**: ✅ Functional (HTTP only)
- REST API integration for RouterOS 7.1+
- Control network interfaces (enable/disable)
- View system resources and configuration
- Execute custom API commands
- **Note**: Currently supports HTTP only - HTTPS support pending SSL certificate configuration

## Tech Stack

- **Language**: Kotlin
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Architecture**: MVVM with Android Architecture Components
- **UI**: Material Design 3 with Bottom Navigation
- **Networking**: Retrofit + OkHttp
- **MikroTik API**: REST API via HTTP/HTTPS

## MikroTik Setup

To use MikroTik control features:

1. Enable www service on your MikroTik router:
   ```
   /ip service set www disabled=no port=80
   ```
   Or use custom port (e.g., 1080):
   ```
   /ip service set www port=1080
   ```

2. In the app:
   - Enter router IP address (e.g., 10.0.0.1)
   - Enter port (e.g., 1080)
   - Keep "Use HTTPS" toggle OFF (HTTP mode)
   - Enter admin credentials
   - Click Connect

3. API Examples:
   - `/interface` - List all interfaces
   - `/system/resource` - System information
   - `/interface/ether1` - Get specific interface

**Security Note**: HTTP is unencrypted. Use only on trusted networks. HTTPS support will be added in future updates.

## Project Structure

```
app/
├── src/main/
│   ├── java/com/kilma/raspberrypi/
│   │   ├── MainActivity.kt
│   │   ├── api/
│   │   │   └── MikrotikApiClient.kt
│   │   └── ui/
│   │       ├── hypervisor/
│   │       ├── docker/
│   │       ├── minecraft/
│   │       ├── homeautomation/
│   │       └── mikrotik/
│   └── res/
│       ├── layout/
│       ├── navigation/
│       ├── menu/
│       └── xml/
│           └── network_security_config.xml
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

- ✅ MikroTik HTTP REST API integration
- ⏳ MikroTik HTTPS support with self-signed certificates
- ⏳ API integration with Raspberry Pi backend
- ⏳ Real-time performance graphs
- ⏳ Push notifications for system alerts
- ⏳ SSH terminal integration
- ⏳ Custom Minecraft command builder

## Known Limitations

- MikroTik HTTPS connections fail due to self-signed certificate handling
- HTTP cleartext traffic must be enabled (configured in network_security_config.xml)
- Raspberry Pi features are UI templates only (backend pending)
