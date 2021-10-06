package net.krupizde.mediaApp.business.serviceImpl

import net.krupizde.mediaApp.business.service.FileService
import org.springframework.stereotype.Service
import java.io.File
@Service
class FileServiceImpl : FileService {
    override fun listFiles(path: String): List<File>? {
        return listFiles(File(path))
    }

    override fun listFiles(file: File): List<File>? {
        return file.listFiles()?.asList()
    }
}