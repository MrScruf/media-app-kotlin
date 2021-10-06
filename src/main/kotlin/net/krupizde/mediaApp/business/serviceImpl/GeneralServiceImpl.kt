package net.krupizde.mediaApp.business.serviceImpl

import net.krupizde.mediaApp.business.service.GeneralService
import net.krupizde.mediaApp.persistence.entity.MyEntity
import net.krupizde.mediaApp.persistence.repository.GeneralRepository
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional

@Transactional
class GeneralServiceImpl<Entity : MyEntity, Repository : GeneralRepository<Entity>>(val repository: Repository) :
    GeneralService<Entity, Repository> {
    override fun findById(id: Long?): Entity? {
        return repository.findById(id);
    }

    override fun find(pageable: Pageable): Page<Entity> {
        return repository.find(pageable)
    }

    override fun deleteById(id: Long) {
        repository.deleteById(id)
    }

    override fun delete(entity: Entity) {
        repository.delete(entity)
    }

    override fun save(entity: Entity): Entity {
        return repository.save(entity)
    }

    override fun saveIfNotExists(entity: Entity, criteria: Example<Entity>): Entity {
        return repository.saveIfNotExists(entity, criteria);
    }

    override fun update(id: Long, entity: Entity): Entity {
        val loaded = findById(id) ?: throw IllegalArgumentException("Entity with id $id does not exist")
        if (entity.id != loaded.id) throw IllegalStateException("Cannot change id of updated entity")
        return repository.save(entity)
    }
}