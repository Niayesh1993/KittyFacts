# KittyFacts 🐱

A simple Android app that displays random cat facts and allows users to save their favorites.

## ✨ Features

* Fetch random cat facts from API
* Save favorite facts locally
* View saved facts
* Loading and error states handling

## 🧱 Architecture

The app follows **Clean Architecture** with separation into:

* **data**: API + database + repository implementations
* **domain**: business logic, models, use cases
* **presentation**: UI (Jetpack Compose) + ViewModel

## 🛠 Tech Stack

* Kotlin
* Jetpack Compose
* Hilt (Dependency Injection)
* Retrofit (Networking)
* Room (Local database)
* Coroutines & Flow

## 🧪 Testing

* Unit tests for ViewModel and UseCases
* Coroutine testing with `kotlinx-coroutines-test`
* Mocking using MockK

## ▶️ How to Run

1. Clone the repo
2. Open in Android Studio
3. Run the app

## 💡 Notes

* The app is built with scalability in mind using clean architecture principles.
* Designed to demonstrate modern Android development best practices.
