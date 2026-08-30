# Firefox VPN Android App

This is an Android VPN client that implements the Firefox VPN (MASQUE protocol) in Kotlin.

## Project Structure

- `app/src/main/java/com/example/firefoxvpn/ui`: Contains the UI activities (Login, Main, Settings)
- `app/src/main/java/com/example/firefoxvpn/vpn`: Contains the VpnService and VPN state
- `app/src/main/java/com/example/firefoxvpn/repository`: Repositories for authentication and Guardian proxy pass
- `app/src/main/java/com/example/firefoxvpn/masque`: Manages the MASQUE connection
- `app/src/main/java/com/example/firefoxvpn/network`: Data classes for API responses
- `app/src/main/res`: Resources (strings, colors, themes, icons)

## Current Status

This project is a **foundation** for a production-ready Firefox VPN Android app. It includes:

- Project setup with Gradle and necessary dependencies (Jetpack Compose, Ktor, etc.)
- Placeholder UI screens following Material Design 3
- Architecture for separating concerns (UI, repositories, VPN service)
- Placeholders for the core VPN logic (authentication, proxy pass retrieval, MASQUE connection)
- GitHub Actions workflow for building the debug APK

## Next Steps for Production

To make this app production-ready, the following components need to be implemented:

### 1. Authentication (`AuthRepository`)
   - Implement Firefox Account login flow using PKCE
   - Handle session token exchange for OAuth token
   - Implement token refresh and secure storage

### 2. Guardian Proxy Pass (`GuardianRepository`)
   - Implement calls to Guardian API to fetch proxy pass
   - Implement proxy pass renewal before expiration
   - Handle token pool (multiple tokens) if needed

### 3. MASQUE Connection (`MasqueConnectionManager`)
   - Establish HTTP/2 or HTTP/3 CONNECT tunnel to the MASQUE proxy
   - Provide raw socket access for sending/receiving IP packets
   - Handle connection keep-alive and reconnects
   - Support for both HTTP/2 and HTTP/3 (based on configuration)

### 4. VPN Service Integration (`FirefoxVpnService`)
   - Use the repositories to authenticate and get proxy pass
   - Establish MASQUE connection
   - Set up VpnService interface (tun-like device)
   - Pump packets between VpnService interface and MASQUE connection
   - Handle VPN lifecycle (start/stop, foreground service)

### 5. UI Integration
   - Connect UI actions to the repositories and VPN service
   - Show real-time connection status
   - Allow users to select locations (which affects proxy pass selection)
   - Implement settings toggles

### 6. Testing and Optimization
   - Test on various Android devices and API levels
   - Optimize battery usage and background behavior
   - Ensure compliance with Google Play VPN policy
   - Add proper error handling and user feedback

## Building the App

To build the app locally:

1. Ensure you have Android Studio Flamingo or later installed
2. Open the `firefox-android-vpn` folder as a project
3. Sync Gradle dependencies
4. Run on an emulator or physical device (API 21+)

The GitHub Actions workflow (`.github/workflows/build.yml`) automatically builds the debug APK on push to main.

## Important Notes

- This app requires the `BIND_VPN_SERVICE` permission and must be set up as a VPN service.
- Actual VPN functionality requires implementing the MASQUE connection, which is non-trivial due to the complexity of HTTP/2/3 and tunnel establishment.
- For production, consider whether to port the Go VPN core to Kotlin or use it via JNI/Native libraries.

## License

[Specify your license here]

*/