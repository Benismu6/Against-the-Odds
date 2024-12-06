# Against the Odds - README

## Overview

**Against the Odds** is a sports analytics and betting odds application designed to provide users with insightful statistics, betting calculations, and interactive features. Developed using modern Android technologies, the app utilizes Kotlin, Jetpack Compose, and a PostgreSQL database for data storage.

**Developed by**:
- Zeshan Basaran
- Benis Munezero
- Sujay Vuchula
- Elliott Kolberg
- Kyle Mattox

---

## Prerequisites

### **Software Requirements**
1. **Android Studio**:
    - Download and install Android Studio (latest stable version) from [Android Studio](https://developer.android.com/studio).
    - Ensure you have Android SDK version 31 or above installed.

2. **PostgreSQL**:
    - Install PostgreSQL 13 or higher.
    - Ensure the PostgreSQL server is running and accessible from your local machine.

3. **JDK**:
    - Install Java Development Kit (JDK) version 11 or higher.

4. **Git** (optional):
    - Install Git for version control if you want to clone the repository.

---

## Installation

### **Clone the Repository**
1. Open a terminal or command prompt.
2. Run the following command to clone the project:
   ```bash
   git clone [https://github.com/<your-repo-link>.git](https://github.com/Benismu6/Against-the-Odds.git)
3. Navigate to the project directory:
   cd against-the-odds

## Set Up Android Studio
1. Open Android Studio.
2. Click on File > Open, and select the project folder.
3. Right click the "app" folder in the left side bar and select "Reload from Disk"
4. Allow Android Studio to sync the Gradle files. If Grade files do not automatically sync, select
   "Gradle" in the right side bar, right click "AgainstTheOdds", and select "Reload Gradle Project".

## Package Installation
1. Open the build.gradle file for the app module.
2. Ensure the following dependencies are present:
    - Jetpack Compose 
    - Retrofit 
    - OkHttp 
    - PostgreSQL JDBC Driver 
    - ViewModel KTX 
    - Navigation Compose
3. If missing, add them to the dependencies section in build.gradle:
```gradle
   dependencies {
       implementation "androidx.compose.material3:material3:1.1.0"
       implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1"
       implementation "androidx.navigation:navigation-compose:2.6.0"
       implementation "com.squareup.retrofit2:retrofit:2.9.0"
       implementation "com.squareup.okhttp3:okhttp:4.9.3"
       implementation "org.postgresql:postgresql:42.5.0"
   }
```
4. Sync the project by clicking "Sync Now" when prompted.

## Running the Project
1. In Android Studio, connect an emulator or a physical device.
2. Click the Run button or press Shift + F10.
3. The app will launch, and data fetching/upload will begin automatically.

# Features
1. Statistics Dashboard:
    - View detailed stats for all NFL teams.
2. Betting Odds Calculator:
    - Calculate confidence levels for over/under bets.
3. Help/FAQ:
    - Access in-app help and FAQs.
4. Settings:
    - Customize app preferences such as notifications and default teams.

# Troubleshooting
## Common Issues
1. NetworkOnMainThreadException
    - Ensure all network/database calls are executed on a background thread.
2. Database Connection Issues
    - Verify PostgreSQL server is running and accessible.
    - Check the DB_URL, DB_USER, and DB_PASSWORD in TeamRepository.kt.
3. Gradle Sync Failures
    - Ensure you have a stable internet connection.
    - Clear Gradle cache
   ```bash
   ./gradlew clean
## Our Fail-Proof Method for Emulator Issues:
1. Reload "app" from disk.
2. Go to Device Manager and stop device.
3. Turn Wi-Fi off and on.
4. Start device.
5. Reload Gradle project.
6. Run app.
## API Issues
If having issues generating statistics data, check [Render.com/games/all](https://nfl-api.onrender.com/games/all).
If there is no data, or connection issues persist, contact zbasara1@students.towson.edu so that the
Render.com web service may be restarted.
