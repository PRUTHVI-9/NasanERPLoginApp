plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("com.google.dagger.hilt.android")
    kotlin("kapt") // Required for annotation processing
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 21
        targetSdk = 35
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
        viewBinding  = true
    }

    packaging {
        resources {
            pickFirsts += setOf(
                "META-INF/INDEX.LIST",
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt"
            )
        }
    }


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.firebase.appdistribution.gradle)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation ("androidx.core:core-ktx:1.12.0")
    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // retrofit gson converter
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // okhttp
    // define a BOM and its version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))

    // define any required OkHttp artifacts without version
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    val lifecycle_version = "2.8.7"

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")

    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    // Coroutine
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Workmanager
    val work_version = "2.10.1"

    // (Java only)
    implementation("androidx.work:work-runtime:$work_version")

    // Kotlin + coroutines
    implementation("androidx.work:work-runtime-ktx:$work_version")

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // glide
    implementation(libs.glide)


    //added

    implementation("io.grpc:grpc-okhttp:1.65.0")

    implementation ("net.sourceforge.jtds:jtds:1.3.1")


    // If grpc-netty got pulled in by mistake, exclude it
    configurations.all {
        exclude(group = "io.grpc", module = "grpc-netty")
        exclude(group = "io.netty")
    }



}