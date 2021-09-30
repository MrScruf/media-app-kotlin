package net.krupizde.mediaApp.presentation.controllers

import net.krupizde.mediaApp.business.service.PersonService
import net.krupizde.mediaApp.persistence.entity.Album
import net.krupizde.mediaApp.persistence.entity.Image
import net.krupizde.mediaApp.persistence.entity.Person
import net.krupizde.mediaApp.persistence.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/persons")
class PersonController(@Autowired service: PersonService) :
    GeneralController<Person, PersonRepository, PersonService>(service) {
    @GetMapping(value = ["/{id}/albums"], produces = ["application/json"], params = ["page", "size"])
    fun getAlbums(
        @PathVariable id: Long,
        @RequestParam(name = "page", required = false) page: Int?,
        @RequestParam(name = "size", defaultValue = "10", required = false) size: Int
    ): ResponseEntity<List<Album>> {
        val albumList = service.findById(id)?.albums
        return createListResponse(albumList, page, size)
    }

    @PostMapping("/{id_person}/albums/{id_album}")
    fun addAlbumToPersonById(@PathVariable id_person: Long, @PathVariable id_album: Long): ResponseEntity<Any> {
        return createResponseWithCall { service.addAlbumToPersonById(id_person, id_album) }
    }

    @PostMapping(value = ["/{id_person}/albums"])
    fun addAlbumToPersonNew(@PathVariable id_person: Long, @RequestBody album: Album): ResponseEntity<Any> {
        return createResponseWithCall { service.addAlbumToPersonNew(id_person, album) }
    }

    @DeleteMapping(value = ["/{id_album}/images/{id_image}"])
    fun removeAlbumFromPerson(@PathVariable id_album: Long, @PathVariable id_image: Long): ResponseEntity<Any> {
        return createResponseWithCall { service.removeAlbumFromPerson(id_album, id_image) }
    }
}