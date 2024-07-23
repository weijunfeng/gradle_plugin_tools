package com.wjf.gradle.plugin.nativecoroutines

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.tasks.PodspecTask

class NativeCoroutinesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val jarPath = this.javaClass.protectionDomain.codeSource.location.path
        NativeBuildType.entries.forEach {
            target.tasks.register(
                "podPublishNativeCoroutines${it.name.lowercase().capitalized()}",
                NativeCoroutinesTask::class.java
            ) { task ->
                task.jarPathDir.set(jarPath)
            }
        }
        target.afterEvaluate {
            target.tasks.withType(PodspecTask::class.java)
                .configureEach { task ->
                    val buildType = task.name.removePrefix("podSpec")
                    if (buildType.isEmpty() || buildType == task.name) {
                        return@configureEach
                    }
                    val podPublishNativeCoroutines = target.tasks.getByName(
                        "podPublishNativeCoroutines${buildType}",
                        NativeCoroutinesTask::class
                    )
                    task.finalizedBy(podPublishNativeCoroutines)
                    podPublishNativeCoroutines.apply {
                        this.podspecFile.set(task.outputFile)
                    }
                }
        }
    }
}