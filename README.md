# DineSmart - Restaurant Discovery Application

A comprehensive restaurant guide application built with modern Android development practices using Kotlin and Jetpack Compose.

## Course Information

- **Course Code:** COMP-3074
- **Course Title:** Mobile Application Development I
- **Professor:** Przemyslaw Pawluk
- **Semester:** Fall 2025
- **Group:** G-55

## Team Members

| Name | Student ID | Role |
|------|-----------|------|
| Prabesh Shrestha | 101538718 | Front-End Development, UI/UX Design, Integration |
| Moksh Chettri | 101515045 | Code Review, Navigation Architecture |

## Project Overview

DineSmart is a modern restaurant discovery application that enables users to explore dining options with comprehensive information including ratings, reviews, location details, and navigation capabilities. The application features a clean, intuitive interface built entirely with Jetpack Compose and follows Material Design 3 guidelines.

### Key Features

- **Complete Restaurant Information:** Detailed restaurant profiles with ratings, cuisine types, addresses, and contact information
- **Interactive Map Integration:** Google Maps SDK integration with custom markers and location-based features
- **Search and Filter:** Advanced filtering by cuisine type, rating, and real-time search functionality
- **User Reviews:** Firebase-powered review system with rating capabilities
- **Navigation Assistance:** Integrated Google Maps directions with multiple travel modes
- **Persistent Storage:** Room database for local data management
- **Responsive Design:** Material 3 design system with glass morphism effects

## Application Architecture

### Navigation Flow

```
SplashScreen (Home)
    ├── RestaurantListScreen (Main)
    │   ├── RestaurantDetailScreen
    │   ├── AddRestaurantScreen
    │   └── Search/Filter Interface
    ├── MapScreen (Google Maps Integration)
    ├── AboutScreen
    └── Navigation Drawer
```

### Screen Descriptions

| Screen | Functionality | Implementation Details |
|--------|--------------|----------------------|
| Splash Screen | Application entry point with branding | Animated logo, featured restaurants carousel |
| Restaurant List | Browse all restaurants with search/filter | LazyColumn with pull-to-refresh, filter chips |
| Restaurant Detail | Comprehensive restaurant information | Reviews, ratings, directions, contact options |
| Add Restaurant | Form to add new restaurant entries | Form validation, image URL support |
| Map Screen | Interactive map with restaurant markers | Google Maps SDK, custom markers, location permissions |
| About Screen | Application and team information | Course details, team credits |

## Technology Stack

### Core Technologies
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Design System:** Material 3
- **Minimum SDK:** 24 (Android 7.0)
- **Target SDK:** 36

### Architecture & Libraries
- **Architecture Pattern:** MVVM (Model-View-ViewModel)
- **Navigation:** Jetpack Navigation Compose
- **Database:** Room Persistence Library
- **Backend:** Firebase (Firestore, Authentication, Storage)
- **Maps:** Google Maps SDK for Android, Maps Compose
- **Image Loading:** Coil
- **Animations:** Lottie Compose
- **Dependency Injection:** ViewModelProvider Factory

### Development Tools
- **IDE:** Android Studio Ladybug (2024.2.1)
- **Build System:** Gradle with Kotlin DSL
- **Version Control:** Git

## UI/UX Design

### Design Principles
- Material Design 3 guidelines
- Glass morphism effects for modern aesthetic
- Consistent color theming and typography
- Responsive layouts for various screen sizes
- Accessibility considerations with semantic content descriptions

### Visual Elements
- Gradient backgrounds with animated orbs
- Elevated cards with shadow effects
- Floating action buttons for primary actions
- Custom glass-style components (cards, buttons, text fields)
- Interactive markers with color coding by rating

## Database Schema

### Restaurant Entity
```
- id: Int (Primary Key)
- name: String
- tags: String (Cuisine types)
- rating: Int (1-5)
- address: String
- phone: String
- latitude: Double (nullable)
- longitude: Double (nullable)
- image: String (nullable, URL)
```

### Review Entity
```
- id: String (Firebase document ID)
- restaurantId: Int
- userId: String
- userName: String
- rating: Float
- comment: String
- timestamp: Long
```

## Setup Instructions

### Prerequisites
1. Android Studio Ladybug or newer
2. JDK 11 or higher
3. Android SDK with API level 24 or higher
4. Google Maps API key
5. Firebase project configuration

### Installation Steps

1. Clone the repository
```bash
git clone [repository-url]
cd DineSmart
```

2. Configure API Keys
   - Create `local.properties` file in project root
   - Add Google Maps API key:
   ```
   MAPS_API_KEY=your_google_maps_api_key_here
   ```

3. Firebase Setup
   - Download `google-services.json` from Firebase Console
   - Place it in `app/` directory

4. Build the Project
   - Open project in Android Studio
   - Sync Gradle dependencies
   - Build > Make Project

5. Run the Application
   - Connect Android device or start emulator
   - Run > Run 'app'

## Features Implementation

### Implemented Features
- Complete UI/UX with 6 main screens
- MVVM architecture with ViewModels and Repository pattern
- Room database for local persistence
- Firebase Firestore for cloud-based reviews
- Google Maps integration with custom markers
- Runtime location permissions handling
- Search and advanced filtering system
- Navigation with deep linking support
- Responsive Material 3 design
- Image loading with Coil
- Animated splash screen with Lottie

### Data Management
- 24 sample restaurants with complete information
- JSON-based initial data loading
- Reactive data flow with Kotlin Flows
- Automatic database initialization

## Project Structure

```
app/src/main/
├── java/com/example/dinesmart/
│   ├── data/
│   │   ├── room/          # Room database entities and DAOs
│   │   ├── firebase/      # Firebase services
│   │   ├── maps/          # Google Maps services
│   │   ├── Restaurant.kt
│   │   ├── Review.kt
│   │   ├── RestaurantViewModel.kt
│   │   ├── RestaurantRepository.kt
│   │   └── ReviewRepository.kt
│   ├── navigation/
│   │   ├── Routes.kt
│   │   └── NavGraph.kt
│   ├── ui/
│   │   ├── components/    # Reusable UI components
│   │   ├── screens/       # Application screens
│   │   └── theme/         # Material 3 theming
│   └── MainActivity.kt
├── assets/
│   └── restaurants.json   # Sample restaurant data
└── res/
    ├── drawable/
    ├── values/
    └── xml/
```

## Testing

- Unit tests for ViewModels and Repository classes
- Instrumented tests for database operations
- UI tests for critical user flows
- Manual testing across multiple device configurations

## Known Limitations

- Requires active internet connection for Firebase and Maps features
- Location features require device GPS capabilities
- Image URLs must be publicly accessible

## Future Enhancements

- User authentication and profiles
- Favorite restaurants feature
- Advanced search with filters
- Restaurant recommendations based on preferences
- Social sharing capabilities
- Offline mode improvements
- Restaurant photo gallery
- Reservation system integration

## License

This project is developed as part of academic coursework for COMP-3074 at George Brown College.

## Acknowledgments

Developed by Group G-55 for Mobile Application Development I
George Brown College, Fall 2025

---

**Note:** This application is a student project developed for educational purposes.

