package net.krupizde.mediaApp.presentation.controllers

import net.krupizde.mediaApp.business.service.FileService
import net.krupizde.mediaApp.presentation.dto.buildFromFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.File

@RestController
@RequestMapping("/files")
@CrossOrigin("*")
class FileController(@Autowired val fileService: FileService) {
    @GetMapping("")
    fun getFiles(@RequestParam path: String): ResponseEntity<*>? {
        val list: List<File> = if (path.isBlank()) File.listRoots().asList() else fileService.listFiles(path)
            ?: throw Exception("Is file, not folder")
        val output = list.map { buildFromFile(it) }
        return createResponseWithCall { output }
    }

    @PostMapping()
    fun addFileToManager(@RequestParam path: String): ResponseEntity<*> {
        return createResponseWithCall { "Ahojda" }
    }
}