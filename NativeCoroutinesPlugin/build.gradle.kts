plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("java-gradle-plugin")
    id("maven-publish")
    id("groovy")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gradleApi())
    implementation(localGroovy())
    implementation(project.gradleKotlinDsl())
    implementation(libs.kotlin.plug)
}

group = "com.wjf.gradle.plugin"
version = "1.0.0"

gradlePlugin {
    plugins {
        create("NativeCoroutinesPlugin") {
            id = "com.wjf.gradle.NativeCoroutinesPlugin"
            implementationClass = "com.wjf.gradle.plugin.nativecoroutines.NativeCoroutinesPlugin"
        }
    }
}

publishing {
    repositories {
        maven {
            credentials {
                username = ""
                password = ""
            }
            isAllowInsecureProtocol = true
            url = uri("${rootProject.buildDir}/repo")
        }
        mavenLocal {
            url = uri("${rootProject.buildDir}/repo")
        }
    }
}