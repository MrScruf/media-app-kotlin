package net.krupizde.mediaApp.business.serviceImpl

import net.krupizde.mediaApp.SORTED_FOLDER_NAME
import net.krupizde.mediaApp.business.service.CollectionService
import net.krupizde.mediaApp.persistence.entity.Album
import net.krupizde.mediaApp.persistence.entity.Collection
import net.krupizde.mediaApp.persistence.entity.Image
import net.krupizde.mediaApp.persistence.repository.AlbumRepository
import net.krupizde.mediaApp.persistence.repository.CollectionRepository
import net.krupizde.mediaApp.persistence.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.pathString

@Service
class CollectionServiceImpl @Autowired constructor(
    repository: CollectionRepository,
    val albumRepository: AlbumRepository,
    val imageRepository: ImageRepository
) :
    GeneralServiceImpl<Collection, CollectionRepository>(repository), CollectionService {

    @Transactional
    override fun addAlbumToCollection(idCollection: Long, album: Album): Album {
        val collection =
            repository.findById(idCollection)
                ?: throw IllegalArgumentException("Collection with id $idCollection does not exists")
        val savedAlbum = albumRepository.save(album)
        processAlbumImages(album);
        savedAlbum.collection = collection
        collection.albums.add(savedAlbum)
        moveAlbumToCollectionFolder(album, collection);
        album.path = Path.of(collection.path, SORTED_FOLDER_NAME, album.name).pathString
        processAlbumImages(album);
        return savedAlbum
    }

    fun moveAlbumToCollectionFolder(album: Album, collection: Collection) {
        Files.move(Path.of(album.path), Path.of(collection.path, SORTED_FOLDER_NAME));
    }

    fun processAlbumImages(album: Album) {
        val images = File(album.path).listFiles() ?: throw IllegalArgumentException("");
        for (image in images) {
            val imageEntity = imageRepository.save(Image(name = image.name, path = image.absolutePath, album = album))
            album.images.add(imageEntity)
        }
    }

    override fun deleteAlbum(idCollection: Long, idAlbum: Long): Boolean {
        TODO("Not yet implemented")
    }
}