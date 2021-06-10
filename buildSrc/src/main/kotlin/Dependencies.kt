object Versions {

    const val gradle = "4.2.0"

    const val minSdk = 25
    const val compileSdk = 30
    const val targetSdk = 30
    const val buildTools = "28.0.3"

    const val kotlin = "1.4.32"
    const val safeArgs = "2.3.5"
    const val ktx =  "1.0.1"
    const val appCompat =  "1.2.0"
    const val constraintLayout  = "1.1.2"
    const val swipeRefreshLayout = "1.1.0"
    const val coroutines  = "1.5.0"
    const val javaInject = "1"
    const val material = "1.3.0"
    const val picasso = "2.71828"
    const val recyclerView = "1.2.1"
    const val hilt =  "2.36"
    const val timber = "4.7.1"
    const val navigation = "2.3.5"
    const val okhttp = "4.9.0"
    const val retrofit = "2.9.0"
    const val gson = "2.8.6"
    const val roomVersion = "2.3.0"

    const val testRunner =  "1.3.0"
    const val junit =  "4.13.1"
    const val testRules = "1.1.0"
    const val mockitoKotlin = "2.2.0"
    const val coroutinesTest = "1.5.0"
    const val kluent = "1.15"
    const val archCore = "2.1.0"
    const val testCore = "1.0.0"
    const val espresso =  "3.3.0"
    const val barista =  "3.9.0"
    const val turbine = "0.5.1"
    const val robolectric = "4.4"
    const val lifecycle = "2.3.0"
}

object Plugins {

    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val hiltGradle = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val safeArgsGradle = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.safeArgs}"
}

object Libs {

    const val kotlinStandardLibrary ="org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val androidKotlinExtensions = "androidx.core:core-ktx:${Versions.ktx}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"

    // dependency injection
    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    const val javaInject = "javax.inject:javax.inject:${Versions.javaInject}"

    // design
    const val materialDesign = "com.google.android.material:material:${Versions.material}"
    const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"

    // coroutines
    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"

    // image loading
    const val picasso = "com.squareup.picasso:picasso:${Versions.picasso}"

    // logging
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    // navigation
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    // database
    const val roomRuntime = "androidx.room:room-runtime:${Versions.roomVersion}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"
    const val roomKaptCompiler = "androidx.room:room-compiler:${Versions.roomVersion}"

    // networking
    const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    // serialization
    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    // unit test
    const val junit = "junit:junit:${Versions.junit}"

    // mock test
    const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}"

    // assertions test
    const val kluent = "org.amshove.kluent:kluent:${Versions.kluent}"

    // core test
    const val archCore = "androidx.arch.core:core-testing:${Versions.archCore}"
    const val testCore = "androidx.test:core:${Versions.testCore}"

    // test runner-rules
    const val testRunner = "androidx.test:runner:${Versions.testRunner}"
    const val testRules = "androidx.test.ext:junit-ktx:${Versions.testRules}"

    // hilt test
    const val hiltTest = "com.google.dagger:hilt-android-testing:${Versions.hilt}"
    const val hiltTestCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"

    // network test
    const val retrofitTest = "com.squareup.retrofit2:retrofit-mock:${Versions.retrofit}"
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.okhttp}"

    // instrumentation test
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val barista = "com.schibsted.spain:barista:${Versions.barista}"

    // flow-coroutines test
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTest}"
    const val turbine = "app.cash.turbine:turbine:${Versions.turbine}"

    // robolectric
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
}
