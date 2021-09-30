package net.krupizde.mediaApp.presentation.controllers

import net.krupizde.mediaApp.business.service.FileService
import net.krupizde.mediaApp.presentation.dto.buildFromFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
@RequestMapping("/files")
class FileController(@Autowired val fileService: FileService) {
    @GetMapping("")
    fun getFiles(@RequestParam path: String): ResponseEntity<*>? {
        val list: List<File> = if (path.isBlank()) File.listRoots().asList() else fileService.listFiles(path)
        val output = list.map { buildFromFile(it) }
        return createResponseWithCall { output }
    }
}