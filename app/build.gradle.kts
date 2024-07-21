plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("android.plugin.baseBuild")
}

android {
    namespace = "com.wk.android_player"

    defaultConfig {
        applicationId = "com.wk.android_player"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {

    implementation(project(mapOf("path" to ":android_player_tencent")))
    implementation(project(mapOf("path" to ":android_player_api")))
    implementation ("com.github.feeeei:CircleSeekbar:v1.1.2")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:2.8.2")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation ("io.reactivex:rxandroid:1.2.1")
    implementation ("com.github.princekin-f:EasyFloat:2.0.4")
}