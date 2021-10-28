package net.krupizde.mediaApp.business.serviceImpl

import net.krupizde.mediaApp.SORTED_FOLDER_NAME
import net.krupizde.mediaApp.business.service.AlbumService
import net.krupizde.mediaApp.persistence.entity.Album
import net.krupizde.mediaApp.persistence.entity.Collection
import net.krupizde.mediaApp.persistence.entity.Image
import net.krupizde.mediaApp.persistence.repository.AlbumRepository
import net.krupizde.mediaApp.persistence.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files
import java.nio.file.Path

@Service
class AlbumServiceImpl(@Autowired repository: AlbumRepository, val imageRepository: ImageRepository) :
    GeneralServiceImpl<Album, AlbumRepository>(repository),
    AlbumService {
    @Transactional
    override fun addImageToAlbumById(idAlbum: Long, idImage: Long) {
        val album = repository.findById(idAlbum)
        val image = imageRepository.findById(idImage)
        if (album == null || image == null)
            throw IllegalArgumentException("Album with id $idAlbum or image with id $idImage does not exist")
        album.images.add(image)
        image.album = album
        val savePath = Path.of(album.collection.path, SORTED_FOLDER_NAME, album.name)
        Files.move(Path.of(image.path), savePath);
        image.path = savePath.toFile().absolutePath
    }

    @Transactional
    override fun removeImageFromAlbum(idAlbum: Long, idImage: Long) {
        val album = repository.findById(idAlbum)
        val image = imageRepository.findById(idImage)
        if (album == null || image == null) throw IllegalArgumentException("Album with id $idAlbum or image with id $idImage does not exist")
        image.album = null
        album.images.remove(image)
    }

    override fun findByNameAndCollection(name: String, collection: Collection): Album? {
        return repository.findByNameAndCollection(name, collection);
    }


}