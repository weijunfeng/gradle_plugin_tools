package com.wjf.gradle.plugin.nativecoroutines

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.util.jar.JarFile

abstract class NativeCoroutinesTask : DefaultTask() {

    @get:Internal
    internal abstract val podspecFile: Property<File>

    @get:Input
    internal abstract val jarPathDir: Property<String>

    @TaskAction
    fun action() {
        val podspecFile = podspecFile.get()
        val destFileDir =
            File(podspecFile.parentFile, "${podspecFile.nameWithoutExtension}/Classes")
        destFileDir.deleteRecursively()
        JarFile(jarPathDir.get()).use { jarFile: JarFile ->
            val entries = jarFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                if (entry.name.startsWith("kmp-nativecoroutines/") && !entry.isDirectory) {
                    val substring = entry.name.substring("kmp-nativecoroutines/".length)
                    val destFile = File(destFileDir, substring)
                    destFile.parentFile.mkdirs()
                    jarFile.getInputStream(entry).use { inputStream: InputStream ->
                        Files.copy(inputStream, destFile.toPath())
                    }
                }
            }
        }
    }
}