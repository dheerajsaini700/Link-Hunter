# 🛡️ Linkhunter

**Linkhunter** is a professional Android security utility designed to intercept, analyze, and safely browse suspicious URLs. It provides an isolated "Secure Sandbox" environment to protect users from phishing, data leakage, and unauthorized hardware access.

![Linkhunter Dashboard](https://img.shields.io/badge/Status-Functional-brightgreen)
![License](https://img.shields.io/badge/License-MIT-blue)

## 🚀 Key Features

*   **Intelligent Intent Interception**: Automatically catches `http` and `https` links from any app (WhatsApp, Messages, etc.).
*   **Neural Link Analysis**: Detects Homograph attacks (fake domains), suspicious keywords, and IP-based URLs.
*   **Secure Sandbox**: 
    *   **Isolated Browser Engine**: Loads links in a private environment separate from your system browser.
    *   **Hardware Shields**: Intercepts and blocks all requests for Camera, Microphone, and Contacts.
    *   **Data Isolation**: No shared cookies or session data with other apps.
*   **3D Modern Dashboard**: Real-time tracking of scanned links and detected threats with a sleek, "Cyber-Hunter" aesthetic.

## 🛠️ Built With

*   **Kotlin** - Core logic and UI
*   **Jetpack Compose** - Modern, reactive 3D UI
*   **WebView API** - Isolated browser engine
*   **Material 3** - Professional design system

## 📸 Screenshots

*(Add your screenshots here after uploading)*

## 🛡️ Security Implementation

Linkhunter implements a `WebChromeClient` that overrides `onPermissionRequest`. This creates a hard-coded "Deny" policy for all hardware permissions, ensuring that even if a user is tricked, the website cannot physically access the device sensors.

## 📜 License

This project is licensed under the MIT License.
