// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()

    }
    dependencies {
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath("com.google.gms:google-services:4.4.1")
        classpath("com.google.gms:google-services:4.4.1")

        val nav_version = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}


plugins {
    id("com.android.application") version "8.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}