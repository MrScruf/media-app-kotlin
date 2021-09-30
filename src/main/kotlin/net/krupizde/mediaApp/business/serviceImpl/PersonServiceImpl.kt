package net.krupizde.mediaApp.business.serviceImpl

import net.krupizde.mediaApp.business.service.PersonService
import net.krupizde.mediaApp.persistence.entity.Album
import net.krupizde.mediaApp.persistence.entity.Person
import net.krupizde.mediaApp.persistence.repository.AlbumRepository
import net.krupizde.mediaApp.persistence.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PersonServiceImpl(@Autowired repository: PersonRepository, @Autowired val albumRepository: AlbumRepository) :
    GeneralServiceImpl<Person, PersonRepository>(repository), PersonService {
    @Transactional
    override fun addAlbumToPersonById(idPerson: Long, idAlbum: Long) {
        val person = repository.findById(idPerson)
        val album = albumRepository.findById(idAlbum)
        if (person == null || album == null)
            throw IllegalArgumentException("Album with id $idAlbum or person with id $idPerson does not exist")
        person.albums.add(album)
        album.person = person
    }

    @Transactional
    override fun addAlbumToPersonNew(idPerson: Long, album: Album): Album {
        val person =
            repository.findById(idPerson) ?: throw IllegalArgumentException("Person with id $idPerson does not exists")
        val savedAlbum = albumRepository.save(album)
        savedAlbum.person = person
        person.albums.add(savedAlbum)
        return savedAlbum
    }

    @Transactional
    override fun removeAlbumFromPerson(idPerson: Long, idAlbum: Long) {
        val person = repository.findById(idPerson)
        val album = albumRepository.findById(idAlbum)
        if (person == null || album == null) throw IllegalArgumentException("Album with id $idAlbum or person with id $idAlbum does not exist")
        album.person = null
        person.albums.remove(album)
    }
}