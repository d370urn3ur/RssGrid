import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.konan.properties.hasProperty
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {

    target {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    dependencies {
        implementation(projects.composeApp)
        implementation(libs.ui.tooling.preview)
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.material3)
    }
}

val keystoreProperties = try {
    val keystorePropertiesFile = rootProject.file("keystore.properties")
    Properties().apply {
        load(FileInputStream(keystorePropertiesFile))
    }
} catch (_: Throwable) {
    Properties()
}

android {

    namespace = "the.autarch.newsgrid"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "the.autarch.android.newsgrid"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 10
        versionName = "2.0.3"
    }
    signingConfigs {
        val keystoreFile = if (keystoreProperties.hasProperty("storeFile")) {
            val keystorePath = keystoreProperties["storeFile"] as String
            file(System.getProperty("user.home") + File.separator + keystorePath)
        } else {
            file(System.getProperty("user.home"))
        }
        create("playstore") {
            storeFile = keystoreFile
            storePassword = keystoreProperties["storePassword"] as? String
            keyAlias = keystoreProperties["keyAlias"]  as? String
            keyPassword = keystoreProperties["keyPassword"] as? String
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    flavorDimensions += "signing"
    productFlavors {
        create("github") {}
        create("playstore") {}
    }
    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            productFlavors.getByName("playstore").signingConfig = signingConfigs.getByName("playstore")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}