# My Menu List - Android App

![Language](https://img.shields.io/badge/Language-Kotlin-blue?style=for-the-badge&logo=kotlin)
![Platform](https://img.shields.io/badge/Platform-Android-green?style=for-the-badge&logo=android)
![License](https://img.shields.io/badge/License-MIT-lightgrey?style=for-the-badge)

A modern Android application for browsing meal recipes, built with a focus on clean architecture, dependency injection, and modern development practices. This project serves as a robust template for building scalable, maintainable, and testable Android apps.

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack & Key Libraries](#tech-stack--key-libraries)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [API Configuration](#-api-configuration)
- [Screenshots](#screenshots)
- [Contributing](#contributing)
- [License](#license)

## Project Overview

My Menu List is an Android application designed to demonstrate a clean, modular architecture using the latest Android development tools. The app allows users to log in to their account and browse a list of meals fetched from a remote server.

The core focus of this repository is on the technical implementation, showcasing:
-   **Dependency Injection** with Dagger Hilt for loose coupling and scalability.
-   **Clean Architecture** (UI → ViewModel → Repository → Remote) to separate concerns.
-   **Secure Networking** with Retrofit and OkHttp, including a robust system for handling JWT authentication and automatic token refreshing.
-   **Modern UI** development with Jetpack Compose.

## Features

-   **User Authentication**: Secure login screen for user authentication.
-   **JWT Token Management**: Handles JWT access and refresh tokens.
-   **Automatic Token Refresh**: The app automatically detects expired access tokens (401 Unauthorized) and uses the refresh token to get a new one without interrupting the user.
-   **Resilient Networking**: Handles network errors, server errors, and displays appropriate messages to the user.
-   **Asynchronous Operations**: Uses Kotlin Coroutines for managing background threads and API calls.
-   **SSL Pinning Bypass (Development Only)**: Includes a configuration to trust self-signed certificates for easier local development.

## Architecture

This project follows the principles of **Clean Architecture** with an **MVVM (Model-View-ViewModel)** pattern. This separation of concerns makes the app more robust, testable, and easier to maintain.

**`UI Layer (Compose)`** → **`ViewModel`** → **`Repository`** → **`Remote Data Source (API)`**

-   **UI Layer (`screens`)**: Built with Jetpack Compose. Observes state from the ViewModel and sends user events to it. It is completely unaware of where the data comes from.
-   **ViewModel (`viewmodels`)**: Acts as a bridge between the UI and the Repository. It holds the UI state, handles user logic, and calls the repository to fetch data. It has no knowledge of the UI framework.
-   **Repository (`repository`)**: The single source of truth for the app's data. It decides whether to fetch data from the network or a local cache. It exposes simple functions for the ViewModel to use.
-   **Remote Data Source (`remote`)**: Handles all communication with the backend API using Retrofit and OkHttp. Includes interceptors for logging and authentication.

## Tech Stack & Key Libraries

-   **Language**: [Kotlin](https://kotlinlang.org/)
-   **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - Android's modern toolkit for building native UI.
-   **Architecture**: MVVM, Clean Architecture
-   **Dependency Injection**: [Dagger Hilt](https://dagger.dev/hilt/) - For managing dependencies and lifecycles.
-   **Asynchronous Programming**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
-   **Networking**:
    -   [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
    -   [OkHttp](https://square.github.io/okhttp/) - The underlying HTTP client, used for interceptors and SSL handling.
    -   [HttpLoggingInterceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor) - For logging network requests and responses.
-   **JSON Parsing**: [Moshi](https://github.com/square/moshi) - A modern JSON library for Android, Kotlin, and Java.
-   **Navigation**: [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation)
-   **Local Storage**: [SharedPreferences](https://developer.android.com/training/data-storage/shared-preferences) - Used by `AuthManager` to securely store authentication tokens.

## Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

-   [Android Studio](https://developer.android.com/studio) (latest stable version recommended)
-   JDK 11 or higher

### Installation

1.  **Clone the repository**
    