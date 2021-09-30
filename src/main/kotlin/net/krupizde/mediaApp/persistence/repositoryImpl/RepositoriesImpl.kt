package net.krupizde.mediaApp.persistence.repositoryImpl

import net.krupizde.mediaApp.persistence.entity.Album
import net.krupizde.mediaApp.persistence.entity.Image
import net.krupizde.mediaApp.persistence.entity.MyEntity
import net.krupizde.mediaApp.persistence.entity.Person
import net.krupizde.mediaApp.persistence.repository.AlbumRepository
import net.krupizde.mediaApp.persistence.repository.GeneralRepository
import net.krupizde.mediaApp.persistence.repository.ImageRepository
import net.krupizde.mediaApp.persistence.repository.PersonRepository
import net.krupizde.mediaApp.persistence.repositoryJpa.AlbumRepositoryJpa
import net.krupizde.mediaApp.persistence.repositoryJpa.ImageRepositoryJpa
import net.krupizde.mediaApp.persistence.repositoryJpa.PersonRepositoryJpa
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

open class GeneralRepositoryImpl<Entity : MyEntity, RepositoryJpa : JpaRepository<Entity, Long>>
    (private val repositoryJpa: RepositoryJpa) : GeneralRepository<Entity> {

    override fun findById(id: Long?): Entity? {
        repositoryJpa.fin
        return repositoryJpa.findByIdOrNull(id)
    }

    override fun deleteById(id: Long) {
        repositoryJpa.deleteById(id)
    }

    override fun delete(entity: Entity) {
        repositoryJpa.delete(entity)
    }

    override fun save(entity: Entity): Entity {
        return repositoryJpa.save(entity)
    }

    override fun find(pageable: Pageable): Page<Entity> {

        return repositoryJpa.findAll(pageable);
    }

    override fun saveIfNotExists(entity: Entity, criteria: Example<Entity>): Entity {
        return repositoryJpa.findOne(criteria).orElseGet { save(entity) }
    }
}

@Repository
class AlbumRepositoryImpl(@Autowired albumRepository: AlbumRepositoryJpa) :
    GeneralRepositoryImpl<Album, AlbumRepositoryJpa>(albumRepository), AlbumRepository

@Repository
class ImageRepositoryImpl(@Autowired imageRepository: ImageRepositoryJpa) :
    GeneralRepositoryImpl<Image, ImageRepositoryJpa>(imageRepository), ImageRepository

@Repository
class PersonRepositoryImpl(@Autowired personRepository: PersonRepositoryJpa) :
    GeneralRepositoryImpl<Person, PersonRepositoryJpa>(personRepository), PersonRepository