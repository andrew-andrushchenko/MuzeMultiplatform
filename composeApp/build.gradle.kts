import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

val generateBuildConfig by tasks.registering(Sync::class) {
    val baseUrl = gradleLocalProperties(rootDir, providers).getProperty("base_url")

    from(
        resources.text.fromString("""
        |package com.andrii_a.muze
        |
        |object BuildConfig {
        |    const val BASE_URL = "$baseUrl"
        |}
        |
      """.trimMargin()
        )
    ) {
        rename { "BuildConfig.kt" } // set the file name
        into("com/andrii_a/muze/") // change the directory to match the package
    }

    into(layout.buildDirectory.dir("generated-src/"))
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            freeCompilerArgs.addAll("-P", "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=com.andrii_a.muze.domain.PlatformParcelize")
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            implementation(libs.room.runtime.android)

            implementation(libs.androidx.core.splashscreen)
        }
        commonMain {
            kotlin.srcDir(generateBuildConfig.map { it.destinationDir })
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.uiUtil)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.bundles.ktor)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.navigation.compose)

            implementation(libs.paging.common)
            implementation(libs.paging.compose.common)

            implementation(libs.coil)
            implementation(libs.coil.compose.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            implementation(libs.kotlinx.serialization.json)

            implementation(libs.constraintlayout)

            implementation(libs.kotlinx.datetime)

            implementation(libs.material3.adaptive)
            implementation(libs.material3.adaptive.layout)
            implementation(libs.material3.adaptive.navigation)

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)

            implementation(libs.ktor.client.okhttp)
        }
    }
}

android {
    namespace = "com.andrii_a.muze"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.andrii_a.muze"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    ksp(libs.room.compiler)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.andrii_a.muze.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.andrii_a.muze"
            packageVersion = "1.0.0"
        }
    }
}