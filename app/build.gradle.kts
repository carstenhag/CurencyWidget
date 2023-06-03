@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinx.serialization)
    id(libs.plugins.kotlinKapt.get().pluginId)
    alias(libs.plugins.daggerHiltAndroidPlugin)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "de.chagemann.currencywidget"
    compileSdk = 33

    defaultConfig {
        applicationId = "de.chagemann.currencywidget"
        minSdk = 25
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        // https://developer.android.com/jetpack/androidx/releases/compose-kotlin
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}



dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material)
    implementation(libs.material3)
    implementation(libs.coroutines.core)
    implementation(libs.retrofit)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.retrofit)
    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.javalite)

    implementation(libs.glance)
    implementation(libs.glance.appwidget)

    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)


    testImplementation(libs.junit)
    kaptTest(libs.dagger.hilt.compiler)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    kaptAndroidTest(libs.dagger.hilt.testing)
}

kapt {
    correctErrorTypes = true
}

protobuf {
    // Configures the Protobuf compilation and the protoc executable
    protoc {
        // Downloads from the repositories
        artifact = "com.google.protobuf:protoc:3.23.2"
    }

    // Generates the java Protobuf-lite code for the Protobufs in this project
    // https://github.com/zhaobozhen/LibChecker/blob/c0c3bc7c661fe45cc44d5c6ab0202764652e0b7e/app/build.gradle.kts
    generateProtoTasks {
        all().forEach {
            it.builtins {
                create("java") {
                    option("lite")
                }
            }

            /*
            it.plugins {
                create("grpc") {
                    option("lite")
                }
            }

             */
        }
    }
}
