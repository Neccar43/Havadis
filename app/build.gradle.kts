plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    alias(libs.plugins.googleGmsGoogleServices)
}

android {
    namespace = "com.novacodestudios.havadis"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.novacodestudios.havadis"
        minSdk = 28
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding=true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.work)

    //Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    implementation (libs.androidx.datastore.preferences)

    implementation (libs.androidx.room.runtime)
    annotationProcessor (libs.androidx.room.room.compiler)
    ksp (libs.androidx.room.room.compiler)
    implementation (libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)

    implementation(libs.coil)

    implementation (libs.work.runtime.ktx)
    //work manager hilt
    implementation (libs.work.runtime.ktx)

    implementation(libs.androidx.lifecycle.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // Saved state module for ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)

    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)
    // Coroutine Lifecycle Scopes
    implementation (libs.androidx.lifecycle.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx)
    //Coroutine firebase integration
    implementation (libs.kotlinx.coroutines.play.services)

    implementation(libs.androidx.paging.runtime)

    implementation(libs.shimmer)

    //Retrofit testi için
    testImplementation(libs.mockwebserver)

    testImplementation(libs.kotlinx.coroutines.test)


}