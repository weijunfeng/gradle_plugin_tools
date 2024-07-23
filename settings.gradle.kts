enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        maven {
            url = uri("/Users/wjf/Documents/github/gradle_plugin_tools/build/repo/")
        }
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("/Users/wjf/Documents/github/gradle_plugin_tools/build/repo/")
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "gradle_plugin_tools"
include(":androidApp")
include(":shared")
include(":NativeCoroutinesPlugin")
