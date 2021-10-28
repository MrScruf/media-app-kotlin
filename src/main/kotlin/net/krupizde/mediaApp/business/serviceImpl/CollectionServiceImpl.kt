package net.krupizde.mediaApp.business.serviceImpl

import net.krupizde.mediaApp.business.service.CollectionService
import net.krupizde.mediaApp.persistence.entity.Album
import net.krupizde.mediaApp.persistence.entity.Collection
import net.krupizde.mediaApp.persistence.repository.AlbumRepository
import net.krupizde.mediaApp.persistence.repository.CollectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CollectionServiceImpl(@Autowired repository: CollectionRepository, @Autowired val albumRepository: AlbumRepository) :
    GeneralServiceImpl<Collection, CollectionRepository>(repository), CollectionService {
    @Transactional
    override fun addAlbumToCollectionById(idCollection: Long, idAlbum: Long) {
        val collection = repository.findById(idCollection)
        val album = albumRepository.findById(idAlbum)
        if (collection == null || album == null)
            throw IllegalArgumentException("Album with id $idAlbum or collection with id $idCollection does not exist")
        collection.albums.add(album)
        album.collection = collection
    }

    @Transactional
    override fun addAlbumToCollectionNew(idCollection: Long, album: Album): Album {
        val collection =
            repository.findById(idCollection) ?: throw IllegalArgumentException("Collection with id $idCollection does not exists")
        val savedAlbum = albumRepository.save(album)
        savedAlbum.collection = collection
        collection.albums.add(savedAlbum)
        return savedAlbum
    }

    /*@Transactional
    override fun removeAlbumFromCollection(idCollection: Long, idAlbum: Long) {
        val collection = repository.findById(idCollection)
        val album = albumRepository.findById(idAlbum)
        if (collection == null || album == null) throw IllegalArgumentException("Album with id $idAlbum or collection with id $idCollection does not exist")
        album.collection = null
        collection.albums.remove(album)
    }*/
}