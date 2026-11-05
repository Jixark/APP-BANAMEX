plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "tech.kidzania.banamex.cajero"
    compileSdk = 35

    defaultConfig {
        applicationId = "tech.kidzania.banamex.cajero"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // URL de la aplicación web que envuelve esta app.
        buildConfigField("String", "CAJERO_URL", "\"https://banamex.kidzania.tech/cajero/\"")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
}
