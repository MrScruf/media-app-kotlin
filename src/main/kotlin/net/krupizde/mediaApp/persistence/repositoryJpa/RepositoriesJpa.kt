package net.krupizde.mediaApp.persistence.repositoryJpa

import net.krupizde.mediaApp.persistence.entity.Album
import net.krupizde.mediaApp.persistence.entity.Image
import net.krupizde.mediaApp.persistence.entity.Person
import org.springframework.data.jpa.repository.JpaRepository

interface AlbumRepositoryJpa : JpaRepository<Album, Long>

interface ImageRepositoryJpa : JpaRepository<Image, Long>

interface PersonRepositoryJpa : JpaRepository<Person, Long>