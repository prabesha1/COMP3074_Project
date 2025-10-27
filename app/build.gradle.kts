import java.util.Properties
import java.io.InputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.dinesmart"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.dinesmart"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val mapsKey: String? = run {
            try {
                val localPropsFile = rootProject.file("local.properties")
                if (localPropsFile.exists()) {
                    val props = Properties()
                    localPropsFile.inputStream().use { stream: InputStream -> props.load(stream) }
                    props.getProperty("MAPS_API_KEY")
                } else {
                    project.findProperty("MAPS_API_KEY") as? String
                }
            } catch (_: Exception) {
                project.findProperty("MAPS_API_KEY") as? String
            }
        }

        if (!mapsKey.isNullOrEmpty()) {
            manifestPlaceholders["com.google.android.geo.API_KEY"] = mapsKey
        } else {
            // If not provided, leave placeholder empty so MapScreen can show helpful message
            manifestPlaceholders["com.google.android.geo.API_KEY"] = ""
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.com.airbnb.lottie.compose)
    // Room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // Google Maps Compose and Play Services Maps
    implementation(libs.com.google.play.services.maps)
    implementation(libs.com.google.maps.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}