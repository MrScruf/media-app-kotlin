package net.krupizde.mediaApp.business.service

import com.github.kilianB.hash.Hash
import net.krupizde.mediaApp.persistence.entity.Album
import net.krupizde.mediaApp.persistence.entity.Image
import net.krupizde.mediaApp.persistence.entity.MyEntity
import net.krupizde.mediaApp.persistence.entity.Person
import net.krupizde.mediaApp.persistence.repository.AlbumRepository
import net.krupizde.mediaApp.persistence.repository.GeneralRepository
import net.krupizde.mediaApp.persistence.repository.ImageRepository
import net.krupizde.mediaApp.persistence.repository.PersonRepository
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.io.File

interface GeneralService<Entity : MyEntity, Repository : GeneralRepository<Entity>> {
    fun findById(id: Long?): Entity?;

    fun find(pageable: Pageable): Page<Entity>

    fun deleteById(id: Long)

    fun delete(entity: Entity)

    fun save(entity: Entity): Entity

    fun update(id: Long, entity: Entity): Entity

    fun saveIfNotExists(entity: Entity, criteria: Example<Entity>): Entity
}

interface ImageService : GeneralService<Image, ImageRepository> {
    fun calculatePHash(image: Image): Hash

    fun compareHashes(image1: Image, image2: Image): Double

    fun compareHashes(idImage1: Long, idImage2: Long): Double

    fun hashFromImageProperty(image: Image): Hash
}

interface AlbumService : GeneralService<Album, AlbumRepository> {
    fun addImageToAlbumById(idAlbum: Long, idImage: Long)

    fun addImageToAlbumNew(idAlbum: Long, image: Image): Image

    fun removeImageFromAlbum(idAlbum: Long, idImage: Long)
}

interface PersonService : GeneralService<Person, PersonRepository> {
    fun addAlbumToPersonById(idPerson: Long, idAlbum: Long)

    fun addAlbumToPersonNew(idPerson: Long, album: Album): Album

    fun removeAlbumFromPerson(idPerson: Long, idAlbum: Long)
}

interface FileService {
    fun listFiles(path: String): List<File>?

    fun listFiles(file: File): List<File>?
}