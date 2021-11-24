package net.krupizde.mediaApp.presentation.controllers

import net.krupizde.mediaApp.business.service.CollectionService
import net.krupizde.mediaApp.persistence.entity.Album
import net.krupizde.mediaApp.persistence.entity.Collection
import net.krupizde.mediaApp.persistence.repository.CollectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/collections")
class CollectionController(@Autowired service: CollectionService) :
    GeneralController<Collection, CollectionRepository, CollectionService>(service) {
    @GetMapping(value = ["/{id}/albums"], produces = ["application/json"], params = ["page", "size"])
    fun getAlbums(
        @PathVariable id: Long,
        @RequestParam(name = "page", required = false) page: Int?,
        @RequestParam(name = "size", defaultValue = "10", required = false) size: Int
    ): ResponseEntity<List<Album>> {
        val albumList = service.findById(id)?.albums
        return createListResponse(albumList, page, size)
    }

    @PostMapping(value = ["/{id_collection}/albums"])
    fun addAlbumToCollection(@PathVariable id_collection: Long, @RequestBody album: Album): ResponseEntity<Any> {
        return createResponseWithCall { service.addAlbumToCollection(id_collection, album) }
    }

    @DeleteMapping(value = ["/{id_collection}/albums/{id_album}"])
    fun deleteAlbum(@PathVariable id_collection: Long, @PathVariable id_album: Long): ResponseEntity<Any> {
        return createResponseWithCall { service.deleteAlbum(id_collection, id_album) }
    }
}