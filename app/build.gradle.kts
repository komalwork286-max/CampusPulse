plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.campuspulse.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.campuspulse.app"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:34.7.0"))
    implementation("com.google.firebase:firebase-auth")

    implementation("com.google.firebase:firebase-firestore")
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.storage)
}
