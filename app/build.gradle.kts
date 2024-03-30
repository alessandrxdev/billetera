plugins {
    id("com.android.application")

}

android {
    namespace = "com.arr.bancamovil"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.arr.bancamovil"
        minSdk = 23
        targetSdk = 34
        versionCode = 5
        versionName = "1.5"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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

    buildFeatures {
        viewBinding = true
        buildConfig = true

    }

}

dependencies {

    val kotlinVersion = "1.9.10"
    val navVersion = "2.7.6"
    val liferecycleVersion = "2.7.0"

    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

    implementation("androidx.lifecycle:lifecycle-livedata:$liferecycleVersion")
    implementation("androidx.navigation:navigation-ui:$navVersion")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:$navVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel:$liferecycleVersion")
    implementation("androidx.appcompat:appcompat:1.7.0-alpha03")
    implementation("androidx.work:work-runtime:2.9.0")
    implementation("androidx.preference:preference:1.2.1")

    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.code.gson:gson:2.10.1")
    
    implementation("org.jsoup:jsoup:1.16.1")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
    
    implementation("com.tbuonomo:dotsindicator:5.0")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    
    implementation("com.github.androidmads:QRGenerator:1.0.1")
    implementation("com.github.thealeksandr:PFLockScreen-Android:1.0.0-beta7")
    implementation("com.github.applifycu:bugsend:1.0.4-alpha3")
    implementation("com.github.vihtarb:tooltip:0.2.0"){ 
    exclude(module = "support-compat") 
    }
    
    
    /* local projects */
    implementation(project(":preferences"))
    
    
    

}
