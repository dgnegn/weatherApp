# Weather App | Jetpack Compose + Hilt + Retrofit + WorkManager

A modern Android application that demonstrates Clean Architecture, Dependency Injection with Hilt, and background processing with WorkManager to provide real-time weather updates.

## 🚀 Features
- **Clean Architecture:** Separated into Data, Domain, and Presentation layers for better maintainability.
- **Hilt DI:** Robust dependency management for Retrofit, WorkManager, and Repositories.
- **Reactive UI:** Built entirely with Jetpack Compose and state-aware ViewModels.
- **Background Updates:** Uses `WorkManager` with `HiltWorker` to fetch weather updates periodically.
- **Connectivity Aware:** Monitors network status to handle offline states gracefully.

## 🛠️ Tech Stack
- **UI:** Jetpack Compose, Material 3
- **Networking:** Retrofit with Kotlinx Serialization
- **DI:** Hilt (Dagger)
- **Background Tasks:** WorkManager
- **Local Storage:** SharedPreferences (Room integration ready)

## 🏗️ Architecture Overview
The project follows a modular structure:
- `data/`: Contains API services, DTOs, Mappers, and Repository implementations.
- `domain/`: Contains business logic (Use Cases) and repository interfaces.
- `presentation/`: Compose screens and ViewModels.
- `di/`: Hilt modules for Network, Repository, and UseCase injections.

## 🔧 Setup & Implementation Highlights

### Hilt Worker Configuration
The app uses a custom configuration to enable Hilt injection inside WorkManager: