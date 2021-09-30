package net.krupizde.mediaApp.persistence.repository

import net.krupizde.mediaApp.persistence.entity.Album
import net.krupizde.mediaApp.persistence.entity.Image
import net.krupizde.mediaApp.persistence.entity.MyEntity
import net.krupizde.mediaApp.persistence.entity.Person
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface GeneralRepository<Entity : MyEntity> {
    fun findById(id: Long?): Entity?;

    fun find(pageable: Pageable): Page<Entity>

    fun deleteById(id: Long)

    fun delete(entity: Entity)

    fun save(entity: Entity): Entity

    fun saveIfNotExists(entity: Entity, criteria: Example<Entity>): Entity
}

interface AlbumRepository : GeneralRepository<Album>
interface ImageRepository : GeneralRepository<Image>
interface PersonRepository : GeneralRepository<Person>