package net.krupizde.mediaApp.persistence.repositoryJpa

import net.krupizde.mediaApp.persistence.entity.Album
import net.krupizde.mediaApp.persistence.entity.Image
import net.krupizde.mediaApp.persistence.entity.Collection
import org.springframework.data.jpa.repository.JpaRepository

interface AlbumRepositoryJpa : JpaRepository<Album, Long> {
    fun findAlbumByNameAndCollection(name: String, collection: Collection): Album?
}

interface ImageRepositoryJpa : JpaRepository<Image, Long>

interface CollectionRepositoryJpa : JpaRepository<Collection, Long>