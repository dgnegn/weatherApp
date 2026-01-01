# TodoApp
Integrating Hilt DI with Room and ViewModel in Android

This project demonstrates how to integrate Hilt Dependency Injection (DI) with Room database and ViewModel in an Android application. The setup allows automatic injection of the database, DAO, repository, and ViewModel, simplifying the architecture and improving testability.

# Features

- Hilt DI for dependency management.
- Room for local database storage.
- ViewModel for UI-related data handling.
- Compose UI integration with Hilt ViewModel.

# Setup Instructions

- Add Hilt dependencies to your build.gradle files.
- Setup the Application class with @HiltAndroidApp.
- Provide Room database and DAO using a Hilt module.
- Inject the repository and ViewModel using Hilt annotations.
- Use hiltViewModel() in your Compose UI to get the ViewModel instance.

# Benefits

- Eliminates manual dependency passing.
- Promotes clean architecture.
- Enhances scalability and maintainability.