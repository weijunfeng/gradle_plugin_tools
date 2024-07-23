package com.wjf.gradle.plugin

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform