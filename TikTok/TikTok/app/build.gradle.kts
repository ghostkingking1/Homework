plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // 1. 移除Compose插件（核心：避免生成Theme.kt）
    // alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.tiktok"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.tiktok"
        minSdk = 24
        targetSdk = 36
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
        // 2. 降级为Java 8（兼容我们的Java代码，避免版本过高导致的兼容问题）
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        // 3. Kotlin JVM目标与Java 8对齐
        jvmTarget = "1.8"
    }
    buildFeatures {
        // 4. 关闭Compose，仅保留DataBinding
        compose = false
        dataBinding = true
    }
    // 5. 移除Compose相关配置（若有）
    // composeOptions {
    //     kotlinCompilerExtensionVersion = "1.5.3"
    // }
}

dependencies {
    // 基础依赖（保留必要的，移除Compose相关）
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // 移除Compose相关依赖
    // implementation(libs.androidx.activity.compose)
    // implementation(platform(libs.androidx.compose.bom))
    // implementation(libs.androidx.compose.ui)
    // implementation(libs.androidx.compose.ui.graphics)
    // implementation(libs.androidx.compose.ui.tooling.preview)
    // implementation(libs.androidx.compose.material3)

    // 保留传统View依赖
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // 新增：ViewModel/LiveData依赖（我们的MVVM架构必需）
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.7.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // 测试依赖（移除Compose测试依赖）
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // androidTestImplementation(platform(libs.androidx.compose.bom))
    // androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    // debugImplementation(libs.androidx.compose.ui.tooling)
    // debugImplementation(libs.androidx.compose.ui.test.manifest)
}