package net.krupizde.mediaApp.presentation.dto;

import java.io.File

data class FileDto(val path: String, val name: String, val extension: String, val folder: Boolean)

fun buildFromFile(f: File): FileDto {
    val name = f.name.ifBlank { f.absolutePath }
    return FileDto(f.absolutePath, name, f.name.substringAfterLast("."), f.isDirectory);
}