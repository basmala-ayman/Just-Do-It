plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")

    // Apply the Compose Gradle plugin (version comes from your version catalog / libs.versions.toml)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.todo.just_do_it"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.todo.just_do_it"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    // Important — use a Compose compiler version compatible with Kotlin 1.9.x
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Use a Compose BOM version compatible with Kotlin 1.9.x
    implementation(platform("androidx.compose:compose-bom:2024.04.00"))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Use the same Compose BOM for androidTest too
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.00"))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Room (kept as you had it)
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    //TESTING DEPENDENCIES
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    androidTestImplementation("androidx.room:room-testing:2.6.1")


}
