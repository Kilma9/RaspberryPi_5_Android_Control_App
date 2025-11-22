# Raspberry Pi Control Android App

Android application to control Raspberry Pi 5 running Proxmox hypervisor, MikroTik router, and Immich photo backup.

## Features

### 🖥️ Hypervisor Control
> ⚠️ **Not yet implemented** - UI template ready, backend integration pending

- Performance dashboard (CPU, memory, disk usage)
- VM control buttons (Start, Shutdown, Force Stop)
- Designed for Proxmox hypervisor integration

### 🐳 Docker Management
> ⚠️ **Not yet implemented** - UI template ready

- Docker container controls
- Container status monitoring
- Quick container actions

### ⛏️ Minecraft Server Control
> ⚠️ **Not yet implemented** - UI template ready

- Pre-programmed server commands
- Server restart functionality
- Game mode switching (Creative/Survival)

### 🌐 MikroTik Router Control
> ✅ **Fully functional** - HTTP REST API

**Features:**
- Real-time router control via REST API (RouterOS 7.1+)
- 10 quick command buttons:
  - System Info & Identity
  - Network Interfaces
  - IP Addresses & Routes
  - Firewall Rules
  - DHCP Leases
  - Wireless Info & Clients
  - System Logs
- Custom API command execution
- JSON response formatting for readability

**Setup:**
1. Enable MikroTik www service: `/ip service set www port=1080`
2. In app: Enter IP (e.g., 10.0.0.1), port (1080), credentials
3. Keep HTTPS toggle OFF (HTTP mode)
4. Click Connect

**Limitations:**
- HTTP only (cleartext traffic enabled via network security config)
- HTTPS fails with self-signed certificates

### 📷 Immich Photo Backup
> ⚠️ **Partial implementation** - Manual upload works, auto backup pending

**Features:**
- Connect to self-hosted Immich server
- Manual photo selection and upload
- Upload progress tracking
- Multiple photo batch uploads

**Not yet implemented:**
- Auto backup functionality
- Duplicate detection
- Background sync
- Upload history tracking

**Setup:**
1. Install Immich on Raspberry Pi (see `IMMICH_SETUP.md`)
2. In app: Enter server URL (http://raspberrypi.local:2283) and API key
3. Click Connect
4. Select photos and upload manually

## Tech Stack

- **Language**: Kotlin 1.9.0
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Build System**: Gradle 8.9 + AGP 8.1.0
- **Architecture**: MVVM with LiveData
- **UI**: Material Design 3 with Bottom Navigation (5 tabs max)
- **Networking**: OkHttp 4.11.0
- **Async**: Kotlin Coroutines 1.7.3
- **View Binding**: Enabled

## Project Structure

```
app/
├── src/main/
│   ├── java/com/kilma/raspberrypi/
│   │   ├── MainActivity.kt
│   │   ├── api/
│   │   │   ├── MikrotikApiClient.kt
│   │   │   └── ImmichApiClient.kt
│   │   └── ui/
│   │       ├── hypervisor/
│   │       ├── docker/
│   │       ├── minecraft/
│   │       ├── mikrotik/
│   │       └── photos/
│   └── res/
│       ├── layout/
│       ├── navigation/
│       ├── menu/
│       ├── mipmap-*/          # Custom app icon (all densities)
│       └── xml/
│           └── network_security_config.xml
```

## Building

```powershell
# Build debug APK
.\gradlew assembleDebug

# Install on device
.\gradlew installDebug

# Run tests
.\gradlew test
```

## Icon Generation

Custom app icon is generated from `ICON_android.jpg` using the included PowerShell script:

```powershell
.\generate_icon.ps1
```

The script:
- Crops to inner dark circle only (excludes outer cyan ring)
- Generates all mipmap densities (mdpi to xxxhdpi)
- Creates circular masked versions for both square and round icons
- Outputs to `app/src/main/res/mipmap-*/`

## Configuration

### Network Security (HTTP Support)
`res/xml/network_security_config.xml`:
- Cleartext traffic enabled globally
- Trusts system and user certificates for self-signed HTTPS

### Permissions
- `INTERNET` - Network access
- `ACCESS_NETWORK_STATE` - WiFi detection
- `READ_MEDIA_IMAGES` - Photo access (Android 13+)
- `READ_EXTERNAL_STORAGE` - Photo access (Android 12-)

## Changelog

### November 22, 2025
**Major Updates:**
- 🐛 **Fixed app crash**: Reduced navigation tabs from 6 to 5 (BottomNavigationView limit)
  - Removed Home Automation tab to stay within Material Design constraints
- 📷 **Added Immich photo backup integration**:
  - New Photos tab with manual upload functionality
  - Server connection testing
  - Multi-photo selection and batch upload
  - Upload progress tracking
  - Note: Auto backup toggle present but not functional yet
- 🎨 **Icon improvements**:
  - Regenerated app icon with proper circular cropping
  - Removed white corners and outer cyan ring
  - Shows only inner dark circle content
  - Transparent background for clean appearance
- 📝 **UI improvements**:
  - Added "not yet implemented" notices to Hypervisor tab
  - Added "auto backup not implemented" notice to Photos tab
  - Disabled non-functional auto backup switch
- 📚 **Documentation**:
  - Created comprehensive `IMMICH_SETUP.md` guide
  - Updated README with current feature status
  - Added changelog section

**Technical Details:**
- Created `ImmichApiClient.kt` for REST API communication
- Created `PhotosFragment.kt` and `PhotosViewModel.kt`
- Added photo picker using ActivityResultContracts
- Configured multipart form data uploads
- Added runtime permissions for photo access
- Updated network security config for Immich HTTP

### Previous Development
- ✅ MikroTik REST API integration with 10 quick commands
- ✅ HTTP cleartext traffic configuration
- ✅ JSON response formatting
- ✅ Custom app icon with PowerShell generation script
- ✅ Material Design 3 UI with bottom navigation
- ✅ MVVM architecture implementation

## Known Issues

1. **BottomNavigationView Limit**: Maximum 5 tabs supported
   - Removed Home Automation tab to accommodate Photos tab
   - Consider NavigationRail or Drawer for 6+ tabs in future
2. **Photo Auto Backup**: Not implemented
   - Manual upload only
   - No duplicate detection
   - No background sync
3. **MikroTik HTTPS**: Self-signed certificate errors
   - HTTP works fine on trusted networks
4. **Hypervisor/Docker/Minecraft**: UI templates only
   - Backend integration pending Raspberry Pi deployment

## Future Enhancements

**High Priority:**
- [ ] Implement photo auto backup with WorkManager
- [ ] Add photo duplicate detection (checksum-based)
- [ ] Background sync for photos (WiFi-only constraint)
- [ ] MikroTik HTTPS certificate pinning

**Medium Priority:**
- [ ] Proxmox API integration for hypervisor control
- [ ] Docker API integration
- [ ] Minecraft RCON integration
- [ ] Real-time performance graphs
- [ ] Push notifications for system alerts

**Low Priority:**
- [ ] NavigationRail for 6+ tabs support
- [ ] Dark mode theme optimization
- [ ] Custom Minecraft command builder
- [ ] SSH terminal integration

## Contributing

This is a personal project for controlling a Raspberry Pi 5 homelab. Features are implemented as needed for personal use.

## License

Private project - All rights reserved

---

**Project Status**: Active Development  
**Last Updated**: November 22, 2025  
**Platform**: Android 7.0+ (API 24+)
