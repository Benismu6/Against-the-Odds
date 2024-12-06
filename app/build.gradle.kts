import java.util.Properties
import java.io.File
import java.io.FileInputStream

// build.gradle.kts

// The plugins block specifies the plugins applied to the project.
// It uses the version catalog for dependency management defined in versions.toml.
plugins {
    alias(libs.plugins.android.application)                // Android application plugin
    alias(libs.plugins.jetbrains.kotlin.android)           // Kotlin Android plugin
    alias(libs.plugins.google.gms.google.services)         // Google Services plugin
}

// The android block configures Android-specific options for the project.
android {
    // Namespace for the application, unique identifier.
    namespace = "edu.towson.cosc435.basaran.againsttheodds"

    // Compile SDK version used to compile the application.
    compileSdk = 34

    // The defaultConfig block defines default settings for the application.
    defaultConfig {
        // Application ID, uniquely identifies the application on the device.
        applicationId = "edu.towson.cosc435.basaran.againsttheodds"
        minSdk = 26                                     // Minimum SDK version the app can run on
        targetSdk = 34                                  // Target SDK version for the app
        versionCode = 1                                  // Version code for the application
        versionName = "1.0"                             // Version name for display purposes

        // Specifies the test instrumentation runner to use for tests.
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Enables the use of the support library for vector drawables.
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties()
        val localPropertiesFile = File(rootProject.projectDir, "local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(FileInputStream(localPropertiesFile))
        }

        val apiKey = properties.getProperty("API_KEY") ?: ""
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
    }

    // Configures the build types for the application.
    buildTypes {
        release {
            isMinifyEnabled = false                       // Disables code shrinking for release builds
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),  // Default ProGuard file
                "proguard-rules.pro"                       // Custom ProGuard rules
            )
        }
    }

    // Configure Java compile options for source and target compatibility.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8    // Source compatibility version
        targetCompatibility = JavaVersion.VERSION_1_8    // Target compatibility version
    }

    // Kotlin compile options.
    kotlinOptions {
        jvmTarget = "1.8"                                // Target JVM version for Kotlin
    }

    // Enables Jetpack Compose features for the project.
    buildFeatures {
        compose = true                                     // Enables Compose support
        buildConfig = true
    }

    // Configures options for Jetpack Compose compilation.
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"         // Version of the Kotlin compiler extension for Compose
    }

    // Configures packaging options for the application.
    packaging {
        resources {
            // Excludes specific files from the packaged application.
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// The dependencies block specifies the libraries required for the application.
dependencies {
    // Core libraries
    implementation(libs.androidx.core.ktx)                       // Android KTX core library
    implementation(libs.androidx.lifecycle.runtime.ktx)         // AndroidX Lifecycle runtime
    implementation(libs.androidx.activity.compose)               // Activity Compose library

    // Jetpack Compose libraries
    implementation(platform(libs.androidx.compose.bom))         // BOM for Compose
    implementation(libs.androidx.ui)                            // Compose UI library
    implementation(libs.androidx.ui.graphics)                   // Compose UI graphics library
    implementation(libs.androidx.ui.tooling.preview)            // Compose UI tooling preview
    implementation(libs.androidx.material3)                     // Material3 library for Compose

    // Room and Firebase libraries
    implementation(libs.androidx.room.common)                   // Room common library
    implementation(libs.firebase.database)                       // Firebase Realtime Database library

    // Navigation libraries
    implementation(libs.androidx.navigation.compose)             // Compose navigation library
    implementation(libs.androidx.navigation.runtime.ktx)        // Navigation runtime KTX

    // Networking library
    implementation(libs.okhttp)                                 // OkHttp library for HTTP requests

    implementation("androidx.cardview:cardview:1.0.0") // For CardView
    implementation("com.google.android.material:material:1.9.0") // For Material components

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("org.postgresql:postgresql:42.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.0")
    implementation(libs.androidx.runtime.livedata)
    implementation("com.aallam.openai:openai-client:3.7.2")

    // Testing libraries
    testImplementation(libs.junit)                              // JUnit for unit testing
    androidTestImplementation(libs.androidx.junit)             // AndroidX JUnit for Android tests
    androidTestImplementation(libs.androidx.espresso.core)      // Espresso core for UI testing
    androidTestImplementation(platform(libs.androidx.compose.bom)) // BOM for Compose testing
    androidTestImplementation(libs.androidx.ui.test.junit4)     // UI testing support for Compose

    // Debugging libraries
    debugImplementation(libs.androidx.ui.tooling)              // UI tooling support for debugging
    debugImplementation(libs.androidx.ui.test.manifest)        // UI test manifest for debugging
}
