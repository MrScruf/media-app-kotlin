package net.krupizde.mediaApp.presentation.controllers

import net.krupizde.mediaApp.business.service.GeneralService
import net.krupizde.mediaApp.persistence.entity.MyEntity
import net.krupizde.mediaApp.persistence.repository.GeneralRepository
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


abstract class GeneralController<Entity : MyEntity,
        Repository : GeneralRepository<Entity>,
        Service : GeneralService<Entity, Repository>>(protected val service: Service) {

    @GetMapping(path = ["/"], produces = ["application/json"], params = ["page", "size"])
    fun get(
        @RequestParam(name = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(name = "size", defaultValue = "10", required = false) size: Int
    ): ResponseEntity<List<Entity>> {
        val output = service.find(PageRequest.of(page, size)).content
        return if (output.isEmpty()) ResponseEntity.notFound().build() else ResponseEntity.ok(output)
    }

    @GetMapping(path = ["/{id}"], produces = ["application/json"])
    fun getById(@PathVariable id: Long): ResponseEntity<Entity> {
        val output = service.findById(id) ?: return ResponseEntity.notFound().build();
        return ResponseEntity.ok(output)
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById(@PathVariable id: Long): ResponseEntity<String> {
        service.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @PutMapping(path = ["/{id}"], consumes = ["application/json"])
    fun update(@PathVariable id: Long, @Validated @RequestBody entity: Entity): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(service.update(id, entity))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.localizedMessage)
        }
    }

    @PostMapping(path = ["/"], consumes = ["application/json"])
    fun create(@Validated @RequestBody entity: Entity): ResponseEntity<Entity> {
        return ResponseEntity.ok(service.save(entity))
    }

}