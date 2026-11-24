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

    }

    // Un APK por cada sección web de Banamex en KidZania.
    flavorDimensions += "site"
    productFlavors {
        create("cajero") {
            dimension = "site"
            applicationId = "tech.kidzania.banamex.cajero"
            resValue("string", "app_name", "Banamex Cajero")
            buildConfigField("String", "SITE_URL", "\"https://banamex.kidzania.tech/cajero/\"")
            buildConfigField("boolean", "USES_CAMERA", "false")
        }
        create("turnos") {
            dimension = "site"
            applicationId = "tech.kidzania.banamex.turnos"
            resValue("string", "app_name", "Banamex Turnos")
            buildConfigField("String", "SITE_URL", "\"https://banamex.kidzania.tech/turnos/\"")
            buildConfigField("boolean", "USES_CAMERA", "false")
        }
        create("tarjeta") {
            dimension = "site"
            applicationId = "tech.kidzania.banamex.tarjeta"
            resValue("string", "app_name", "Banamex Tarjeta")
            buildConfigField("String", "SITE_URL", "\"https://banamex.kidzania.tech/tarjeta/\"")
            buildConfigField("boolean", "USES_CAMERA", "true") // escaneo de QR
        }
        create("bancadigital") {
            dimension = "site"
            applicationId = "tech.kidzania.banamex.bancadigital"
            resValue("string", "app_name", "Banamex Banca Digital")
            buildConfigField("String", "SITE_URL", "\"https://banamex.kidzania.tech/bancadigital/\"")
            buildConfigField("boolean", "USES_CAMERA", "true") // escaneo de QR
        }
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
