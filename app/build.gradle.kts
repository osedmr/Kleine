plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
    id ("androidx.navigation.safeargs.kotlin")
    id ("dagger.hilt.android.plugin")
    id ("kotlin-kapt")
    id("com.google.gms.google-services")



}

android {
    namespace = "com.example.kelineyt"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kelineyt"
        minSdk = 24
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation ("androidx.core:core-ktx:1.13.1")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.annotation:annotation:1.8.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")


    //firebase
    implementation ("com.google.firebase:firebase-firestore:25.0.0")
    implementation ("com.google.firebase:firebase-firestore-ktx:25.0.0")
    // implementation ("com.google.firebase:firebase-auth-ktx:22.3.1")
    // implementation ("com.google.firebase:firebase-storage")
    // implementation ("com.google.firebase:firebase-storage-ktx:20.3.0")
    // implementation ("com.google.firebase:firebase-common-ktx:20.4.2")
    // implementation ("com.google.firebase:firebase-messaging-ktx:23.4.1")
    // implementation ("com.firebaseui:firebase-ui-auth:4.3.2")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.0")

    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")

    //intuit
   // implementation ("com.intuit.sdp:sdp-android:1.0.6")
   // implementation ("com.intuit.ssp:ssp-android:1.0.6")


    //loading button
    implementation ("br.com.simplepass:loading-button-android:2.2.0")



    //google play services
   // implementation ("com.google.android.gms:play-services-auth:21.0.0")

    //smooth bar
    //implementation ("com.github.ibrahimsn98:SmoothBottomBar:1.7.9")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.13.0")

    //circular image
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //Navigation and safe args

    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")

    //viewpager2 indicatior
    implementation ("io.github.vejei.viewpagerindicator:viewpagerindicator:1.0.0-alpha.1")

    //lifecycle
   // implementation ("android.arch.lifecycle:extensions:1.1.1")

    //Firebase coroutines
   implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    //stepView
    implementation ("com.shuhart.stepview:stepview:1.5.1")

    //Android Ktx
    implementation ("androidx.fragment:fragment-ktx:1.6.2")


    //Dagger hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation ("com.google.android.material:material:1.12.0") // En son sürüm olduğundan emin olun





}