package net.krupizde.mediaApp.business.serviceImpl

import net.krupizde.mediaApp.IMAGE_EXTENSIONS
import net.krupizde.mediaApp.RANDOM_FOLDER_NAME
import net.krupizde.mediaApp.SORTED_FOLDER_NAME
import net.krupizde.mediaApp.business.service.AlbumService
import net.krupizde.mediaApp.business.service.FileService
import net.krupizde.mediaApp.business.service.ImageService
import net.krupizde.mediaApp.business.service.CollectionService
import net.krupizde.mediaApp.persistence.entity.Album
import net.krupizde.mediaApp.persistence.entity.Image
import net.krupizde.mediaApp.persistence.entity.Collection
import org.apache.logging.log4j.kotlin.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption


@Service
class FileServiceImpl @Autowired constructor(
    val collectionService: CollectionService,
    val albumService: AlbumService,
    val imageService: ImageService
) : FileService {
    private val logger = logger()


    override fun listFiles(path: String): List<File>? {
        return listFiles(File(path))
    }

    override fun listFiles(file: File): List<File>? {
        return file.listFiles()?.asList()
    }

    override fun initialSort(file: File) {
        logger.info("Starting")
        val folders = file.listFiles() ?: throw IllegalArgumentException("")
        folders.forEach {
            if (it.isDirectory) {
                logger.info("Working on folder ${it.absolutePath}")
                if (!it.canRead() || !it.canWrite()) throw Exception("Cannot read or write to ${it.path}")
                val collection =
                    Collection(name = it.name, path = it.absolutePath);
                processCollection(collectionService.saveIfNotExists(collection, Example.of(collection)));
            }
        }
    }

    @Transactional
    fun processCollection(collection: Collection) {
        val collectionFile = File(collection.path)
        val files = collectionFile.listFiles() ?: throw IllegalArgumentException("")
        if (!files.any { it.name.lowercase() == SORTED_FOLDER_NAME.lowercase() } && !Path.of(
                collectionFile.absolutePath, SORTED_FOLDER_NAME
            ).toFile().mkdirs())
            throw Exception("")
        if (!files.any { it.name.lowercase() == RANDOM_FOLDER_NAME.lowercase() } && !Path.of(
                collectionFile.absolutePath, RANDOM_FOLDER_NAME
            ).toFile().mkdirs())
            throw Exception("")
        for (filename in files) {
            if (filename.name.lowercase() != SORTED_FOLDER_NAME.lowercase() && filename.name.lowercase() != RANDOM_FOLDER_NAME.lowercase()) {
                logger.info(
                    "Moving ${filename.toPath()} to ${
                        Path.of(
                            collectionFile.absolutePath, RANDOM_FOLDER_NAME, filename.name
                        )
                    }"
                )
                Files.move(
                    filename.toPath(), Path.of(collectionFile.absolutePath, RANDOM_FOLDER_NAME, filename.name),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        processSorted(collection)
        processRandom(collection)
    }

    fun processSorted(collection: Collection) {
        val collectionSortedFile = Path.of(collection.path, SORTED_FOLDER_NAME).toFile()
        logger.info("Processing sorted of ${collection.name}")
        val albums = collectionSortedFile.listFiles() ?: throw Exception("")
        for (album in albums) {
            logger.info("Processing album ${album.absolutePath}")
            var albumEntity = Album(name = album.name, collection = collection)
            albumEntity = albumService.saveIfNotExists(albumEntity, Example.of(albumEntity))
            processImagesTree(album, albumEntity)
        }
    }

    fun processRandom(collection: Collection) {
        var album = Album(name = RANDOM_FOLDER_NAME, collection = collection)
        album = albumService.saveIfNotExists(album, Example.of(album))
        logger.info("Processing random of ${collection.name}")
        processImagesTree(Path.of(collection.path, RANDOM_FOLDER_NAME).toFile(), album)
    }

    fun processImagesTree(file: File, album: Album) {
        val files = file.listFiles() ?: return
        logger.info("Processing tree of ${file.absolutePath} to album ${album.name}")
        for (processedFile in files) {
            if (processedFile.isDirectory) processImagesTree(processedFile, album)
            if (processedFile.extension !in IMAGE_EXTENSIONS) continue
            else {
                logger.debug("Processing image ${processedFile.absolutePath}")
                var image = Image(name = processedFile.name, path = processedFile.absolutePath, album = album);
                image = imageService.saveIfNotExists(image, Example.of(image))
                logger.debug("Saved image ${image.path}")
                album.images.add(image)
            }
        }
    }
}