package net.krupizde.mediaApp.presentation.controllers

import net.krupizde.mediaApp.business.service.AlbumService
import net.krupizde.mediaApp.persistence.entity.Album
import net.krupizde.mediaApp.persistence.entity.Image
import net.krupizde.mediaApp.persistence.repository.AlbumRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/albums")
class AlbumController(@Autowired service: AlbumService) :
    GeneralController<Album, AlbumRepository, AlbumService>(service) {
    @GetMapping(value = ["/{id}/images"], produces = ["application/json"], params = ["page", "size"])
    fun getImages(
        @PathVariable id: Long,
        @RequestParam(name = "page", required = false) page: Int?,
        @RequestParam(name = "size", defaultValue = "10", required = false) size: Int
    ): ResponseEntity<List<Image>> {
        val imageList = service.findById(id)?.images
        return createListResponse(imageList, page, size)
    }

    @PostMapping("/{id_album}/images/{id_image}")
    fun addImageToAlbumById(@PathVariable id_album: Long, @PathVariable id_image: Long): ResponseEntity<Any> {
        return createResponseWithCall { service.addImageToAlbumById(id_album, id_image) }
    }

    @PostMapping(value = ["/{id_album}/images"])
    fun addImageToAlbumNew(@PathVariable id_album: Long, @RequestBody image: Image): ResponseEntity<Any> {
        return createResponseWithCall { service.addImageToAlbumNew(id_album, image) }
    }

    @DeleteMapping(value = ["/{id_album}/images/{id_image}"])
    fun removeImageFromAlbum(@PathVariable id_album: Long, @PathVariable id_image: Long): ResponseEntity<Any> {
        return createResponseWithCall { service.removeImageFromAlbum(id_album, id_image) }
    }
}