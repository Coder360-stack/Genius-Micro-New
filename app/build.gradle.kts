plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.geniousmicro"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.geniousmicro"
        minSdk = 24
        targetSdk = 34
        versionCode = 5
        versionName = "1.2.2"//new
        setProperty("archivesBaseName", "GeniousMicro-V-$versionName")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures{
        viewBinding= true
        dataBinding = true
    }
}

dependencies {


    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    implementation(libs.glide)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.exoplayer)

    implementation (libs.circleimageview)
    implementation ("com.google.android.material:material:1.2.1")

    implementation("com.android.volley:volley:1.2.1")
    implementation("com.itextpdf:itextg:5.5.10")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.airbnb.android:lottie:3.4.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.10.0") //json reader
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation("com.github.MrNouri:DynamicSizes:1.0")
    implementation ("com.daimajia.easing:library:2.0@aar")
    implementation ("com.daimajia.androidanimations:library:2.3@aar")
    ///SQL DATABASE CONNECTION

    implementation("net.sourceforge.jtds:jtds:1.3.1")

    implementation ("com.github.CanHub:Android-Image-Cropper:3.1.3")


<<<<<<< HEAD


=======
>>>>>>> a7950c80503642a42f360b8df071c530ae832800
}