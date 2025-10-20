plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.remindmev2"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.remindmev2"
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
}

dependencies {
    // Versiones compatibles con AGP 8.5.1 y compileSdk 34
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.9.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.androidx.activity)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth:22.3.1")
}

//dependencies {
//   implementation(libs.androidx.core.ktx)
//  implementation(libs.androidx.appcompat)
//  implementation(libs.material)
//  implementation(libs.androidx.activity)
//  implementation(libs.androidx.constraintlayout)
//
//  testImplementation(libs.junit)
//  androidTestImplementation(libs.androidx.junit)
//  androidTestImplementation(libs.androidx.espresso.core)

    // Firebase Authentication
//  implementation("com.google.firebase:firebase-auth:22.3.1")
//}