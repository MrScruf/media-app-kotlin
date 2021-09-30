package net.krupizde.mediaApp.presentation.controllers

import net.krupizde.mediaApp.business.service.ImageService
import net.krupizde.mediaApp.persistence.entity.Image
import net.krupizde.mediaApp.persistence.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/images")
class ImageController(@Autowired service: ImageService) :
    GeneralController<Image, ImageRepository, ImageService>(service) {


}